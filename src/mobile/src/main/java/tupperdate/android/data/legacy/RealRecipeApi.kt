package tupperdate.android.data.legacy

import android.content.ContentResolver
import android.net.Uri
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import tupperdate.android.data.legacy.api.RecipeApi
import tupperdate.android.data.legacy.api.readFileAsBase64
import tupperdate.common.dto.NewRecipeDTO
import tupperdate.common.dto.RecipeAttributesDTO

@ObsoleteTupperdateApi
class RealRecipeApi(
    private val client: HttpClient,
    private val contentResolver: ContentResolver,
) : RecipeApi {

    override fun like(recipe: RecipeApi.Recipe) {
    }

    override fun dislike(recipe: RecipeApi.Recipe) {
    }

    override val backStackEnabled: Flow<Boolean>
        get() = flowOf(false)

    override fun back() {
    }

    override suspend fun create(
        title: String,
        description: String,
        vegetarian: Boolean,
        warm: Boolean,
        hasAllergens: Boolean,
        imageUri: Uri?,
    ) {
        try {
            client.post<Unit>("/recipes") {
                // TODO : Improve client-side image handling.
                // TODO : Compress images.
                body = NewRecipeDTO(
                    title = title,
                    description = description,
                    attributes = RecipeAttributesDTO(
                        vegetarian = vegetarian,
                        hasAllergens = hasAllergens,
                        warm = warm,
                    ),
                    imageBase64 = imageUri?.readFileAsBase64(contentResolver),
                )
            }
        } catch (problem: Throwable) {
            problem.printStackTrace()
        }
    }
}
