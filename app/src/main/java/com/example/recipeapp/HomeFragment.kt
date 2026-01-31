package com.example.recipeapp

import android.content.Intent
import android.health.connect.datatypes.units.Length
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var recommendationRecipeAdapter: RecommendationRecipeAdapter

    private lateinit var savedRecipeRepository: SavedRecipeRepository

    private var savedRecipeIds = mutableSetOf<String>()

    private lateinit var recipeOfWeekAdapter: RecipeOfWeekAdapter
    private var allRecipe = listOf<Recipe>()
    private var recommendedRecipe = listOf<Recipe>()

    private var recipeWeek = listOf<Recipe>()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)



        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchEdit.setOnClickListener {
            binding.searchEdit.hint = ""
            binding.searchBar.hint = ""

        }

        binding.searchEdit.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                binding.searchEdit.hint = ""
                binding.searchBar.hint = ""
            } else {
                if (binding.searchEdit.text.isNullOrEmpty()) {
                    binding.searchEdit.hint = "Search any recipe"

                }
            }

        }

        savedRecipeRepository = SavedRecipeRepository(requireContext())

        auth = FirebaseAuth.getInstance()

        setUpRecipeData()
        setUpRecyclerView()
        setUpChipFilter()
        setLogOut()
        setUpGreeting()
        observeSavedRecipeIds()


    }

    private fun setUpGreeting() {
        val userName = auth.currentUser?.displayName ?: auth.currentUser?.email ?: "User Name"

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greetingMsg = when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            else -> "Good Evening"
        }

        binding.greetingMsg.text = greetingMsg
        binding.userName.text = userName
    }

    private fun setLogOut() {

        binding.userProfile.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(
                requireContext(), "User has been logout successfully", Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

    }

    private fun setUpChipFilter() {

        binding.chipGroupCategory.setOnCheckedStateChangeListener { _, checkedIds ->
            when {
                checkedIds.contains(binding.chipSeeAll.id) -> filterRecipe("")
                checkedIds.contains(binding.soup.id) -> filterRecipe("Soup")
                checkedIds.contains(binding.salad.id) -> filterRecipe("Salad")
                checkedIds.contains(binding.breakfast.id) -> filterRecipe("Breakfast")
                checkedIds.contains(binding.lunch.id) -> filterRecipe("Lunch")
                checkedIds.contains(binding.dinner.id) -> filterRecipe("Dinner")
                else -> filterRecipe("")

            }
        }

    }

    private fun filterRecipe(category: String) {
        val filtered = if (category.isEmpty()) {
            allRecipe.take(4)
        } else {
            allRecipe.filter { it.category == category }
        }

        recommendationRecipeAdapter.updateList(filtered)
        recipeOfWeekAdapter.updateList(filtered)

    }

    private fun setUpRecipeData() {

        allRecipe = listOf(
            Recipe(
                name = "Pasta Carbonara",
                imageUrl = "https://www.cookingclassy.com/wp-content/uploads/2020/10/spaghetti-carbonara-01.jpg",
                cookingDuration = "30 Min",
                description = "An indulgent Roman classic, Pasta Carbonara is a deceptively simple dish that relies on perfect timing and high-quality ingredients. The magic comes from a silky emulsion of raw eggs and finely grated Parmesan (or pecorino) that cooks only from the heat of freshly drained pasta, creating a glossy sauce that clings to each strand. Crispy pan-fried bacon (or guanciale if you have it) adds a salty, smoky contrast while a generous crack of black pepper gives the dish aromatic bite. Although the ingredient list is short, the result is creamy, savory, and comforting — a restaurant-level plate you can make at home with attention to technique.",
                category = "Lunch",
                ingredients = "Spaghetti – 200 g (about 2 servings)\n• Eggs – 2 large (room temperature)\n• Parmesan cheese – 1/2 cup (finely grated; use pecorino-romano for sharper flavor)\n• Bacon – 100 g (or guanciale/pancetta), diced\n• Black pepper – 1–2 teaspoons freshly cracked\n• Salt – for pasta water (about 1 tbsp)\n• Reserved pasta water – 1/3 to 1/2 cup (hot)\n• Optional: 1 small clove garlic, smashed (for frying the bacon briefly)",
                directions = "1. Prep: Grate the parmesan finely and set in a bowl. Crack the eggs into a separate bowl and whisk until homogenous. Bring a large pot of water to a rolling boil and salt it generously (it should taste like sea water).\n\n2. Cook pasta: Add spaghetti and cook until just al dente (check package time and subtract 1 minute). You want the pasta slightly firmer than fully soft because it will finish in the sauce.\n\n3. Crisp the bacon: While pasta cooks, heat a large skillet over medium heat. Add diced bacon and cook, stirring occasionally, until fat renders and pieces are golden and crisp (6–8 minutes). If using garlic, add it to the pan for 30–60 seconds and remove before it browns.\n\n4. Reserve water: Before draining, scoop out 1/3–1/2 cup of the hot pasta water and set aside. Drain the pasta quickly — do not rinse.\n\n5. Emulsify off-heat: Remove the skillet from direct heat. Add the drained pasta to the skillet with bacon and toss to coat in the fat. Immediately pour the whisked eggs over the hot pasta and begin tossing or stirring vigorously with tongs. The residual heat will gently cook the eggs into a creamy sauce rather than scrambled eggs.\n\n6. Adjust texture: If the sauce looks too thick or clings too stiffly, add the reserved pasta water a tablespoon at a time while tossing to create a silky, emulsified consistency.\n\n7. Finish: Add grated Parmesan and continue tossing until evenly distributed. Season with plenty of freshly cracked black pepper and taste for salt (Parmesan and bacon are salty, so be conservative).\n\n8. Serve: Plate immediately while warm. Optionally top with extra grated cheese and another crack of black pepper. Tips: work quickly and keep the pan off direct heat when adding eggs — the aim is a creamy coating, not scrambled curds.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),

            Recipe(
                name = "Caesar Salad",
                imageUrl = "https://cdn.loveandlemons.com/wp-content/uploads/2024/12/caesar-salad.jpg",
                cookingDuration = "15 Min",
                description = "Caesar Salad is a timeless composed salad built on crisp romaine, crunchy croutons, and a boldly flavored dressing that balances creamy, tangy, and umami notes. The classic dressing combines anchovy (optional), garlic, egg yolk, lemon, mustard, and parmesan to create a velvety emulsion that cloaks each leaf. When made well, the contrasts — cool lettuce, warm croutons, sharp cheese, and bright lemon — come together for a satisfying, restaurant-quality starter or light meal.",
                category = "Salad",
                ingredients = " Romaine lettuce – 1 large head (core removed, leaves washed and chilled)\n• Croutons – 1–2 cups (homemade: cubed bread tossed in olive oil and toasted)\n• Parmesan – 1/3 cup finely grated + extra shavings for garnish\n• Caesar dressing – about 3–4 tablespoons (see steps for quick homemade)\n• Lemon – 1/2 (juice to taste)\n• Salt & freshly ground black pepper – to taste\n• Optional: 1–2 anchovy fillets (minced) or 1/2 tsp anchovy paste\n• Optional: 1 egg yolk (for traditional emulsion) or 1 tbsp mayonnaise (for safety/shortcut)",
                directions = "1. Make croutons (if homemade): Preheat oven to 180°C/350°F. Toss cubed day-old bread in a little olive oil, salt, and optional garlic powder. Spread on a baking sheet and bake 8–12 minutes until crisp and golden, turning once.\n\n2. Quick dressing (traditional emulsion): In a small bowl, mash minced anchovy and crushed garlic to a paste. Add 1 egg yolk (or 1 tbsp mayo), 1 tsp Dijon mustard, and whisk while slowly adding 2–3 tbsp olive oil until emulsified. Stir in 1 tbsp lemon juice and 2 tbsp grated Parmesan. Season to taste with black pepper and a pinch of salt. If you prefer a lighter dressing, add a tablespoon of water or extra lemon juice to thin.\n\n3. Prepare lettuce: Dry the romaine leaves thoroughly (use a salad spinner). Chop into bite-sized pieces or tear by hand for texture. Keep chilled until ready to dress.\n\n4. Toss gently: Place lettuce in a large bowl. Add croutons and 2–3 tbsp dressing. Toss lightly but thoroughly so each leaf is coated; add more dressing little by little if needed.\n\n5. Plate and finish: Transfer to serving plates, sprinkle with extra grated parmesan or shavings, crack a little black pepper over the top, and finish with a tiny squeeze of lemon for brightness. Serve immediately so croutons stay crisp.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),

            Recipe(
                name = "Tomato Soup",
                imageUrl = "https://www.indianhealthyrecipes.com/wp-content/uploads/2022/11/tomato-soup-recipe.jpg",
                cookingDuration = "25 Min",
                description = "This Tomato Soup is a lush, velvety bowl that highlights ripe tomato sweetness rounded by butter and cream. Starting with sautéed aromatics and slowly cooked tomatoes creates depth; blending smooth and finishing with cream yields a luxurious mouthfeel. Fresh or canned tomatoes both work — use the ripest you can find and balance acidity with a touch of sweetness. Garnish ideas: basil leaves, a swirl of cream, croutons, or a shaving of parmesan.",
                category = "Soup",
                ingredients = " Tomatoes – 4 large ripe or 1 can (400 g) whole peeled\n• Onion – 1 medium, finely chopped\n• Garlic – 3 cloves, minced\n• Butter – 1 tbsp (or olive oil)\n• Vegetable or chicken stock – 1 cup\n• Heavy cream – 2 tbsp (optional for richness)\n• Fresh basil – a few leaves (or 1/2 tsp dried)\n• Salt, black pepper – to taste\n• Optional: 1 tsp sugar (if tomatoes are too acidic)\n• Optional garnish: croutons, drizzle of cream, chopped basil",
                directions = "1. Prep tomatoes: If using fresh tomatoes, core and roughly chop. For a smoother soup, blanch in boiling water 30–60 seconds, transfer to ice water, then peel and chop.\n\n2. Sauté aromatics: In a medium pot, melt butter over medium heat. Add chopped onion and cook until soft and translucent (6–8 minutes). Add garlic and cook for 30–60 seconds until fragrant — do not brown.\n\n3. Add tomatoes: Stir in chopped tomatoes and cook until they begin to break down and release juices (5–8 minutes). If using canned tomatoes, break them up with a spoon.\n\n4. Simmer: Add stock, bring to a gentle simmer, and cook 10–12 minutes to develop flavor. Taste and add a pinch of sugar if the tomatoes taste overly acidic.\n\n5. Blend: Remove from heat and use an immersion blender until completely smooth, or carefully transfer to a blender in batches. For ultra-silky texture, pass through a fine sieve.\n\n6. Finish: Return to the pot, stir in the cream and torn basil leaves, and warm gently. Do not boil after adding cream. Adjust salt and pepper.\n\n7. Serve: Ladle into bowls, garnish with a swirl of cream, extra basil, and croutons or toasted bread. Leftovers keep well and often taste even better the next day as flavors meld.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),

            Recipe(
                name = "Pancakes",
                imageUrl = "https://www.foodandwine.com/thmb/HVbJsZlSG7BQF1mif2Z5tZICM8g=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Buttermilk-Pancakes-FT-RECIPE1222-5589088e52c94e6f8a610b4393196fbb.jpg",
                cookingDuration = "20 Min",
                description = "These pancakes are tender, pillowy, and lightly golden — a classic breakfast comfort. The key is to mix until just combined (a few lumps are fine) and to cook on a medium-hot pan so the exterior browns while the interior stays fluffy. Serve stacked with butter, maple syrup, or fresh fruit for a weekend treat.",
                category = "Breakfast",
                ingredients = " All-purpose flour – 1 cup (120 g)\n• Granulated sugar – 2 tbsp\n• Baking powder – 1 tbsp\n• Pinch of salt\n• Milk – 3/4 cup (180 ml) (buttermilk gives extra tenderness)\n• Egg – 1 large\n• Melted butter – 1 tbsp (plus extra for cooking)\n• Optional: 1 tsp vanilla extract or 1/2 cup blueberries/chocolate chips",
                directions = "1. Mix dry ingredients: In a bowl, whisk flour, sugar, baking powder, and salt until evenly combined.\n\n2. Mix wet ingredients: In another bowl, whisk milk, egg, melted butter, and vanilla if using.\n\n3. Combine carefully: Pour wet into dry and stir gently until just combined. A few small lumps are okay — overmixing produces tough pancakes.\n\n4. Preheat pan: Heat a non-stick skillet or griddle over medium heat and brush with a little butter. The surface is ready when a water droplet sizzles and evaporates slowly.\n\n5. Cook pancakes: Pour 1/4 cup batter per pancake; if adding fruit like blueberries, sprinkle them onto the batter now. Cook until bubbles form on the surface and edges look set (2–3 minutes). Flip and cook the second side until golden (another 1–2 minutes).\n\n6. Keep warm and serve: Stack on a plate and keep warm in a low oven if making a large batch. Serve with a pat of butter, warm maple syrup, fresh fruit, or yogurt.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),

            Recipe(
                name = "Grilled Chicken",
                imageUrl = "https://www.lecremedelacrumb.com/wp-content/uploads/2024/05/grilledbbqchicken-8b-3-scaled.jpg",
                cookingDuration = "45 Min",
                description = "This herb-marinated grilled chicken yields juicy, flavorful breasts with a caramelized exterior and tender interior. A simple marinade of olive oil, garlic, fresh herbs, and lemon brightens the meat and helps it stay moist on the grill. Perfect for a protein-forward main dish served with roasted vegetables, salad, or rice.",
                category = "Dinner",
                ingredients = " Chicken breasts – 2 (around 150–200 g each)\n• Olive oil – 2 tbsp\n• Garlic – 3 cloves, minced\n• Fresh herbs – 1 tbsp each chopped rosemary, thyme, or mixed herbs (or 1 tsp dried)\n• Lemon juice – 2 tbsp\n• Salt – 1 tsp\n• Black pepper – 1/2 tsp\n• Optional: 1 tsp honey or mustard in the marinade for extra glaze",
                directions = "1. Butterfly or pound: If breasts are thick, butterfly or gently pound to even thickness (~1–1.5 cm) so they cook evenly.\n\n2. Make marinade: In a bowl, whisk olive oil, minced garlic, chopped herbs, lemon juice, salt, pepper, and optional honey/mustard.\n\n3. Marinate: Coat chicken thoroughly and marinate for at least 20–30 minutes at room temperature or up to 4 hours in the fridge. If chilled, bring to room temperature 20 minutes before grilling.\n\n4. Preheat grill/pan: Heat a grill or grill pan to medium-high. Oil the grates or brush the pan with oil to prevent sticking.\n\n5. Grill: Place chicken on the hot grill, cook without moving for 5–7 minutes until grill marks form. Flip and cook another 5–7 minutes. Internal temperature should reach 75°C / 165°F (use a thermometer inserted into the thickest part).\n\n6. Rest: Transfer to a cutting board and rest 5 minutes to let juices redistribute.\n\n7. Slice & serve: Slice against the grain and serve with your chosen sides. Tip: Baste with reserved marinade warmed briefly (only if cooked) for extra gloss and flavor.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),

            Recipe(
                name = "Spaghetti Bolognese",
                imageUrl = "https://www.preciouscore.com/wp-content/uploads/2024/06/Spaghetti-Bolognese-Chicken.jpg",
                cookingDuration = "40 Min",
                description = "Spaghetti Bolognese is a comforting bowl of tender pasta topped with a slow-simmered meat sauce that’s rich with tomatoes, aromatics, and herbs. Building flavor layers — browning the meat well, gently caramelizing the onion, and allowing the sauce to simmer — yields a deep, savory profile. Serve with grated parmesan and a crisp green salad for a complete meal.",
                category = "Dinner",
                ingredients = " Spaghetti – 200 g\n• Ground meat – 150 g (beef, pork, or chicken)\n• Onion – 1 small, finely chopped\n• Garlic – 2 cloves, minced\n• Olive oil – 1 tbsp\n• Tomato sauce or crushed tomatoes – 1 cup (or 400 g canned)\n• Italian herb mix – 1 tsp dried oregano + 1 tsp basil\n• Salt & pepper – to taste\n• Optional: splash of red wine (1/4 cup), grated carrot for sweetness, parmesan to serve",
                directions = "1. Prepare pasta: Bring a large pot of salted water to a boil and cook spaghetti to al dente according to package, then drain reserving some pasta water.\n\n2. Brown aromatics: In a skillet, heat olive oil over medium heat. Add chopped onion and cook until soft and translucent (6–8 minutes). Add garlic and cook for 30 seconds until fragrant.\n\n3. Brown meat: Increase heat slightly and add ground meat. Break into small pieces and cook until no longer pink and some caramelization appears — this adds deep flavor.\n\n4. Deglaze & simmer: If using, pour in a splash of red wine and scrape up browned bits. Add tomato sauce or crushed tomatoes, Italian herbs, and a pinch of salt. If sauce seems too acidic, add a small grated carrot or a pinch of sugar.\n\n5. Simmer: Reduce heat and simmer gently for 15–20 minutes, uncovered, until the sauce thickens and flavors concentrate. Add a little reserved pasta water if you want a looser sauce.\n\n6. Combine & serve: Toss sauce with drained spaghetti or plate pasta and spoon sauce on top. Finish with grated parmesan and fresh basil if available.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),

            Recipe(
                name = "Greek Salad",
                imageUrl = "https://www.cookingclassy.com/wp-content/uploads/2018/02/greek-salad-4.jpg",
                cookingDuration = "10 Min",
                description = "Greek Salad is a fresh, colorful plate inspired by the Mediterranean: crisp cucumbers, ripe tomatoes, sharp red onion, briny olives, and creamy feta tossed simply in olive oil and herbs. It’s bright, texturally varied, and perfect as a side or light main. Use good-quality extra-virgin olive oil and ripe produce to get the best results.",
                category = "Salad",
                ingredients = " Cucumber – 1 large, peeled or unpeeled, chopped\n• Tomatoes – 2 medium, cut into wedges\n• Red onion – 1/4 thinly sliced\n• Kalamata or black olives – 1/3 cup\n• Feta cheese – 100 g, cubed or crumbled\n• Extra-virgin olive oil – 2 tbsp\n• Dried oregano – 1/2 tsp\n• Salt & pepper – to taste\n• Optional: splash of red wine vinegar or lemon juice",
                directions = "1. Chop vegetables: Cut cucumber and tomatoes into bite-sized pieces and place in a mixing bowl. Thinly slice red onion and add.\n\n2. Assemble: Add olives and cubes of feta.\n\n3. Dress simply: Drizzle olive oil and a small splash of red wine vinegar or lemon if desired. Sprinkle dried oregano, season with salt and freshly ground pepper.\n\n4. Toss lightly: Gently toss to combine without breaking up the feta too much. Serve immediately so vegetables remain crisp. Tip: allow to sit 10 minutes to let flavors meld if making ahead.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),

            Recipe(
                name = "French Onion Soup",
                imageUrl = "https://www.simplyrecipes.com/thmb/SY2lL2neXpYOvkxhYvoUcl6pXLQ=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/Simply-Recipes-French-Onion-Soup-LEAD-2-757941da129647dc90e490ed72b3807c.jpg",
                cookingDuration = "50 Min",
                description = "French Onion Soup is a deeply savory, soul-warming dish built on the slow caramelization of a large quantity of onions until they become sweet, jammy, and richly flavored. The caramelized onions are simmered in beef (or vegetable) stock and aromatics, ladled into bowls, and finished with toasted bread and a blanket of melted cheese — the epitome of rustic comfort food with complex, layered flavors.",
                category = "Soup",
                ingredients = " Yellow onions – 4 large (about 1.2 kg), thinly sliced\n• Butter – 2 tbsp (or mix butter + olive oil)\n• Garlic – 2 cloves, minced\n• Beef broth – 3 cups (sub vegetable broth for vegetarian)\n• Dry white wine or sherry – 1/4 cup (optional)\n• Fresh thyme – a few sprigs (or 1/2 tsp dried)\n• Baguette slices – 4–6, toasted\n• Cheese – 3/4 cup grated Gruyère (or Swiss/Emmental)\n• Salt & pepper – to taste",
                directions = "1. Slice onions thinly: Use a sharp knife to ensure even slices so they cook uniformly.\n\n2. Caramelize slowly: In a large heavy-bottomed pot, melt butter over medium-low heat. Add sliced onions and a pinch of salt. Cook gently, stirring frequently, for 25–30 minutes. The onions should become deeply golden-brown and sweet — patience here is crucial; do not rush by turning up the heat.\n\n3. Deglaze: Add minced garlic and cook 30–60 seconds. If using wine or sherry, pour it in now and scrape up any fond from the bottom of the pot. Allow the alcohol to evaporate for a minute.\n\n4. Add stock and thyme: Pour in beef broth and add thyme. Bring to a simmer and cook 12–15 minutes to meld flavors. Taste and season with salt and pepper.\n\n5. Prepare toasts: While the soup simmers, preheat the broiler. Place baguette slices on a sheet tray, toast until crisp, then sprinkle generously with grated Gruyère.\n\n6. Serve with cheese: Ladle hot soup into ovenproof bowls, place a cheesy toast on top of each bowl, and broil until the cheese is bubbly and golden (watch closely to avoid burning).\n\n7. Finish & enjoy: Carefully remove from oven (bowls will be hot), garnish with extra thyme, and serve immediately. Tips: For a smoother finish, skim off excess fat from the soup surface before broiling; use caution when placing bowls under the broiler.",
                userId = "",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )


        )

        recommendedRecipe = allRecipe.takeLast(4)

        recipeWeek = allRecipe.take(4)


    }


    private fun setUpRecyclerView() {

        recommendationRecipeAdapter = RecommendationRecipeAdapter(
            recommendedRecipe,

            onRecipeClick = { recipe ->
                onRecipeClicked(recipe)

            },

            onFavoriteClick = { recipe ->
                onFavoriteClicked(recipe)
            },

            isFavorite = { recipeId ->
                savedRecipeIds.contains(recipeId)
            }

        )

        binding.rvRecommendation.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationRecipeAdapter
            setHasFixedSize(true)
        }

        recipeOfWeekAdapter = RecipeOfWeekAdapter(
            recipeWeek,

            onRecipeClick = { recipe ->
                onRecipeClicked(recipe)
            },

            onFavoriteClick = { recipe ->
                onFavoriteClicked(recipe)
            },

            isFavorite = { recipeId ->
                savedRecipeIds.contains(recipeId)

            }
        )


        binding.rvRecipeOfWeek.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recipeOfWeekAdapter
            setHasFixedSize(true)
        }
    }

    private fun onFavoriteClicked(recipe: Recipe) {

        lifecycleScope.launch {
            try {
                val recipeData = recipe.toRecipeDataClass()

                val nowSaved = savedRecipeRepository.toggleSaveStatus(recipeData)

                val message = if (nowSaved) {
                    "Recipe ${recipe.name} is Saved "
                } else {
                    "Recipe ${recipe.name} is unsaved"
                }

                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {

                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun onRecipeClicked(recipe: Recipe) {

        val fragment = RecipeDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable("recipe", recipe)
            }
        }


        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .addToBackStack(null).commit()

        Toast.makeText(context, "Clicked:${recipe.name}", Toast.LENGTH_SHORT).show()

    }


    private fun observeSavedRecipeIds() {
        savedRecipeRepository.getAllSavedRecipeId().observe(viewLifecycleOwner) { ids ->
            savedRecipeIds.clear()
            savedRecipeIds.addAll(ids)

            recommendationRecipeAdapter.notifyDataSetChanged()
            recipeOfWeekAdapter.notifyDataSetChanged()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}