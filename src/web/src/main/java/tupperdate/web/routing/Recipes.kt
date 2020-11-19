package tupperdate.web.routing

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import tupperdate.common.model.Recipe
import tupperdate.web.autoId
import tupperdate.web.await

fun Routing.recipes(firestore: Firestore) {
    route("/recipes") {
        val recipeCollection = firestore.collection("recipes/")
        get("next") {
            var recipe: QueryDocumentSnapshot?
            do {
                recipe = recipeCollection
                    .whereGreaterThan("id", autoId())
                    .orderBy("id")
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .getOrNull(0)
            } while (recipe == null)

            call.respond(recipe.toObject(Recipe::class.java))
        }
    }
}
