/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.ui.theme.DessertClickerTheme
import com.example.dessertclicker.model.Dessert

// tag for logging
/*
The priority of the log message, that is, how important the message is. In this case, the Log.v()
logs verbose messages. Log.d() method writes a debug message. Other methods in the Log class include
 Log.i() for informational messages, Log.w() for warnings, and Log.e() for error messages.
 */
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
/*
    The onCreate() method is where you should do any one-time initializations for your activity
    The onCreate() lifecycle method is called once, just after the activity initializes—when the OS creates the new Activity object in memory.
    After onCreate() executes, the activity is considered created.
    When you override the onCreate() method, you must call the superclass
    implementation to complete the creation of the Activity, so within it,
    you must immediately call super.onCreate(). The same is true for other
    lifecycle callback methods.
* */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")
        setContent {
            DessertClickerTheme {
                DessertClickerApp()
            }
        }
    }
/*
After onStart() runs, your activity is visible on the screen.
Unlike onCreate(), which is called only once to initialize your activity,
onStart() can be called by
the system many times in the lifecycle of your activity
*/
/*
When an activity starts from the beginning, you see all three of these lifecycle
callbacks called in order:

onCreate() when the system creates the app.
onStart() makes the app visible on the screen, but the user is not yet able to interact with it.
onResume() brings the app to the foreground, and the user is now able to interact with it.
 */
/*
You've learned that when an app is started and onStart() is called, the app
becomes visible on the screen. When onResume() is called, the app gains the user
focus–that is, the user can interact with the app. The part of the lifecycle in
which the app is fully onscreen and has user focus is called the foreground lifetime.

When the app goes into the background, the focus is lost after onPause(), and the
app is no longer visible after onStop()

The difference between focus and visibility is important. An activity can be
partially visible on the screen but not have the user focus
 */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }
// The onRestart() method is a place to put code that you only want to call if
// your activity is not being started for the first time.
    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }
/*
When onPause() is called, the app no longer has focus. After onStop(),
the app is no longer visible on screen. Although the activity is stopped,
the Activity object is still in memory in the background.
The Android OS has not destroyed the activity. The user might
return to the app, so Android keeps your activity resources around.

Whatever code runs in onPause() blocks other things from displaying,
so keep the code in onPause() lightweight

Both onResume() and onPause() have to do with focus. The onResume() method is called
when the
activity gains focus, and onPause() is called when the activity loses focus.
 */
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }
/*
onCreate() and onDestroy(), which this codelab teaches later,
are only called once during the lifetime of a single activity
instance: onCreate() to initialize the app for the very first time,
and onDestroy() to nullify, close, or destroy objects that the activity
may have been using so that they don't continue to use resources, like memory.

the Back button causes the activity (and the app) to be removed from the screen and moved
to the back of the activity stack.
*/
/*
Your activity does not close down entirely every time the user navigates away from that activity:

When your activity is no longer visible on screen, the status is known as putting the activity
into the background. The opposite of this is when the activity is in the foreground, or onscreen.
When the user returns to your app, that same activity is restarted and becomes visible again.
This part of the lifecycle is called the app's visible lifecycle.
When your app is in the background, it generally should not be actively running to preserve system
resources and battery life. You use the Activity lifecycle and its callbacks to know when your app is moving to the background so that you can pause any ongoing operations. You then restart the operations when your app comes into the foreground.
*/

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}

/**
 * Determine which dessert to show.
 */
fun determineDessertToShow(
    desserts: List<Dessert>,
    dessertsSold: Int
): Dessert {
    var dessertToShow = desserts.first()
    for (dessert in desserts) {
        if (dessertsSold >= dessert.startProductionAmount) {
            dessertToShow = dessert
        } else {
            // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
            // you'll start producing more expensive desserts as determined by startProductionAmount
            // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
            // than the amount sold.
            break
        }
    }

    return dessertToShow
}

/**
 * Share desserts sold information using ACTION_SEND intent
 */
private fun shareSoldDessertsInformation(intentContext: Context, dessertsSold: Int, revenue: Int) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            intentContext.getString(R.string.share_text, dessertsSold, revenue)
        )
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)

    try {
        startActivity(intentContext, shareIntent, null)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            intentContext,
            intentContext.getString(R.string.sharing_not_available),
            Toast.LENGTH_LONG
        ).show()
    }
}

/*
Composable functions have their own lifecycle that is independent
of the Activity lifecycle.
Its lifecycle is composed of the events: enters the Composition,
recomposing 0 or more times, and then leaving the Composition.

In order for Compose to track and trigger a recomposition, it needs to know when
state has changed. To indicate to Compose that it should track an
object's state, the object needs to be of type State or MutableState.
The State type is immutable and can only be read.
A MutableState type is mutable and allows reads and writes.

To instruct Compose to retain and reuse its value during recompositions,
 you need to declare it with the remember API

While Compose remembers the revenue state during recompositions, it does not retain
this state during a configuration change. For Compose to
retain the state during a configuration change, you must use rememberSaveable.

A configuration change occurs when the state of the device changes so
radically that the easiest way for the system to resolve the change is
to completely shut down and rebuild the activity. For example, if the user
changes the device language, the whole layout might need to change to
accommodate different text directions and string lengths

You use the rememberSaveable function to save values that you need if
Android OS destroysand recreates the activity.

To save values during recompositions, you need to use remember.
Use rememberSaveable to save values during recompositions AND
configuration changes.
 */
@Composable
private fun DessertClickerApp(
    dessertViewModel: DesertViewModel = viewModel()
) {

    val dessertUiState by dessertViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            val intentContext = LocalContext.current
            AppBar(
                onShareButtonClicked = {
                    shareSoldDessertsInformation(
                        intentContext = intentContext,
                        dessertsSold = dessertUiState.dessertsSold,
                        revenue = dessertUiState.revenue
                    )
                }
            )
        }
    ) { contentPadding ->
        DessertClickerScreen(
            revenue = dessertUiState.revenue,
            dessertsSold = dessertUiState.dessertsSold,
            dessertImageId = dessertUiState.currentDessertImageId,
            onDessertClicked = {
                dessertViewModel.sell()
            },
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
private fun AppBar(
    onShareButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = 16.dp),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h6,
        )
        IconButton(
            onClick = onShareButtonClicked,
            modifier = Modifier.padding(end = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share),
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    @DrawableRes dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(dessertImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .align(Alignment.Center)
                        .clickable { onDessertClicked() },
                    contentScale = ContentScale.Crop,
                )
            }
            TransactionInfo(revenue = revenue, dessertsSold = dessertsSold)
        }
    }
}

@Composable
private fun TransactionInfo(
    revenue: Int,
    dessertsSold: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White),
    ) {
        DessertsSoldInfo(dessertsSold)
        RevenueInfo(revenue)
    }
}

@Composable
private fun RevenueInfo(revenue: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.total_revenue),
            style = MaterialTheme.typography.h4
        )
        Text(
            text = "$${revenue}",
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
private fun DessertsSoldInfo(dessertsSold: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.dessert_sold),
            style = MaterialTheme.typography.h6
        )
        Text(
            text = dessertsSold.toString(),
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview
@Composable
fun MyDessertClickerAppPreview() {
    DessertClickerTheme {
        DessertClickerApp()
    }
}
