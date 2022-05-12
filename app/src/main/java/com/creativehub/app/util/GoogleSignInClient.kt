package com.creativehub.app.util

import android.content.Context
import com.creativehub.app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
	val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
		.requestIdToken(context.getString(R.string.gcp_client_id))
		.requestId()
		.requestProfile()
		.requestEmail()
		.build()
	return GoogleSignIn.getClient(context, signInOptions)
}