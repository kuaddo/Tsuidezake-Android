import okhttp3.RequestBody.Companion.toRequestBody

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.github.ben-manes.versions")
    id("com.apollographql.apollo")
}
apply<CommonBuildPlugin>()

apollo {
    generateKotlinModels.set(true)
    generateAsInternal.set(true)
    sealedClassesForEnumsMatching.set(listOf(".*"))
}

dependencies {
    implementation(project(":model"))
    implementation(project(":data:auth"))

    implementation(Deps.Kotlin.stdlib)
    implementation(Deps.Kotlin.coroutinesCore)
    implementation(Deps.Kotlin.coroutinesPlayServices)
    implementation(Deps.Firebase.storage)
    api(Deps.Dagger.core)
    kapt(Deps.Dagger.compiler)
    implementation(Deps.OkHttp.core)
    implementation(Deps.OkHttp.loggingInterceptor)
    implementation(Deps.Apollo.runtime)
    implementation(Deps.Apollo.coroutines)
    implementation(Deps.timber)
}

tasks.register("updateGraphQLConfig") {
    val apiKey: String? by project
    val email: String? by project
    val password: String? by project
    apiKey ?: return@register
    email ?: return@register
    password ?: return@register

    val token = getToken(apiKey!!, email!!, password!!)
    if (token != null) {
        writeAuthorizationToken(
            "src/main/graphql/jp/kuaddo/tsuidezake/data/remote/.graphqlconfig",
            token
        )
        println(".graphqlconfig is successfully updated.")
    } else {
        println("getToken() is failed.")
    }
}

fun writeAuthorizationToken(path: String, token: String) {
    file(path).writeText(
        """
            {
                "name": "Tsuidezake GraphQL Schema",
                "schemaPath": "./schema.json",
                "extensions": {
                "endpoints": {
                    "Default GraphQL Endpoint": {
                        "url": "https://us-central1-tsuidezake.cloudfunctions.net/graphql",
                        "introspect": true,
                            "headers": {
                              "Authorization": "$token"
                            }
                        }
                    }
                }
            }
        """.trimIndent()
    )
}

fun getToken(apiKey: String, email: String, password: String): String? {
    val client = okhttp3.OkHttpClient()
    val url =
        "https://www.googleapis.com/identitytoolkit/v3/relyingparty/verifyPassword?key=$apiKey"
    val body = "{\"email\": \"$email\", \"password\": \"$password\", \"returnSecureToken\": true}"
        .toRequestBody()
    val request = okhttp3.Request.Builder()
        .url(url)
        .header("Content-Type", "application/json")
        .post(body)
        .build()
    val response = client.newCall(request).execute()
    val result = response.body?.string() ?: return null

    val gson = com.google.gson.Gson()
    val parsedResult = gson.fromJson(result, GetTokenResult::class.java)
    return parsedResult.idToken
}

class GetTokenResult(
    val kind: String,
    val localId: String,
    val email: String,
    val displayName: String,
    val idToken: String,
    val registered: Boolean,
    val refreshToken: String,
    val expiresIn: String
)
