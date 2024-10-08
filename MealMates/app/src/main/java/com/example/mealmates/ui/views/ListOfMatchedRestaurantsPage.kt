package com.example.mealmates.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealmates.apiCalls.RestaurantsApi
import com.example.mealmates.models.GetPlaceDetailsResponse
import com.example.mealmates.models.Matched
import com.example.mealmates.models.MealMatesPlace
import com.example.mealmates.models.Restaurants
import com.example.mealmates.ui.theme.MealMatesTheme
import com.example.mealmates.ui.theme.component_colour
import com.example.mealmates.ui.theme.primary_text_colour
import com.example.mealmates.ui.theme.selectableList_colour
import com.example.mealmates.ui.viewModels.LoginViewModel
import com.google.gson.Gson

@Composable
fun ListOfMatchedRestaurantsPage(
    loginModel: LoginViewModel,
    restaurantId: Int,
    onNavigateToMainPage: () -> Unit
) {

    MealMatesTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
//                    Image


            /*testing from here------------------------------------------------------------*/
            val grRes: Restaurants = RestaurantsApi().getSingleRestaurants(restaurantId.toString());
            val matchedInfo: Matched = Gson().fromJson(grRes.matched.toString(), Matched::class.java)
            println("Got the info")
            val sortedRestaurants = mutableListOf(matchedInfo.liked[0] to matchedInfo.rids[0])
            for(i in 1 ..<matchedInfo.rids.size){
                sortedRestaurants.add(matchedInfo.liked[i] to matchedInfo.rids[i])
            }
            sortedRestaurants.sortWith(compareByDescending{ it.first })

            //make a list of class MealMatesPlace, in order of sortedRestaurants List array
            var restoList = mutableListOf<MealMatesPlace>()
            for(i in 0 ..<sortedRestaurants.size){
                val placeResponse:String = getPlaceDetails(sortedRestaurants[i].second)
                val Resto = GetPlaceDetailsResponse(placeResponse).getMealMatesPlace()
                restoList.add(Resto)
            }
            //UIs
            ImageSectionListOfMatched(restoList[0])
            Divider(color = Color.Black, thickness = 1.dp)
            TopRestaurantInfoSectionListOfMatched(restoList[0])
            AllergenInfoMessageCard()
            //first value of the list is the liked, second is rid. sort by order of liked num
            //TODO: add algorithm if needed. For now, Ordering only based on number of likes
            /*testing until here------------------------------------------------------------*/

            //Others section
            OtherTopPicksSectionListOfMatched()

            Column(
            ) {
                val photoCacheA = remember { mutableMapOf<String, ByteArray>() }
                for(i in 1..<restoList.size ){

                    val thisPlace = restoList[i]
                    Column(
                        modifier = Modifier
                            .height(102.dp)
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                            .fillMaxWidth()
                            .background(color = component_colour, RoundedCornerShape(20.dp))

                        ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            //TODO:when fetchPlacePhoto is called, in most of cases it jams the emulator.
                            //Todo:when it is fixed, add Image section.
//                            val PhotoVal = thisPlace.photos[0].photoReference
//                            var photoByteArrayA = photoCacheA[PhotoVal]
//                            if (photoByteArrayA == null) {
//                                photoCacheA[PhotoVal] = fetchPlacePhoto(PhotoVal)
//                                val temp = photoCacheA[PhotoVal]
//                                photoByteArrayA = temp
//                            }
//                            Image(
//                                bitmap = convertImageByteArrayToBitmap(photoByteArrayA!!).asImageBitmap(),
//                                contentScale = ContentScale.Crop,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .clip(CircleShape)
//                                ,
//                            )
                            Icon(
                                Icons.Default.CheckCircle,
                                "Place",
                                tint = primary_text_colour,
                                modifier = Modifier
                                    .size(60.dp),
                            )
                            Column (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 0.dp, horizontal = 20.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ){
                                val PlaceName = thisPlace.displayName
                                val PlaceAddress = thisPlace.shortFormattedAddress
                                Text(PlaceName, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = primary_text_colour)
                                var adSize:TextUnit
                                if(PlaceAddress.length < 60){
                                    adSize = 15.sp
                                }else{
                                    adSize = 10.sp
                                }
                                Text(PlaceAddress, fontSize = adSize, color = primary_text_colour)
                            }
                        }


                    }
                }
            }

//            Text(text = "aa")
//            ListRestaurantsListOfMatched(restoList.size, restoList)//using constant for demo - will be
            //receiving proper var in further development


        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
        }

    }

}


@Composable
fun ImageSectionListOfMatched(place: MealMatesPlace){
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val photoByteArrayA = fetchPlacePhoto(place.photos[0].photoReference)
        Image(
            bitmap = convertImageByteArrayToBitmap(photoByteArrayA!!).asImageBitmap(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
            ,
        )
//        AsyncImage(
//            model = RESTAURANT_DATA[2].photos[0],
//            contentDescription = "Image for top rated restaurant",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize())//hard coded for now
        Text("Image of top restaurant match")
    }
}

@Composable
fun TopRestaurantInfoSectionListOfMatched(place: MealMatesPlace){
    Column(
        modifier = Modifier
            .height(75.dp)
            .fillMaxWidth()
            .background(color = component_colour)
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ){
        Text(place.displayName, color= primary_text_colour)
        Spacer(modifier = Modifier.padding(5.dp))
        Text(place.shortFormattedAddress, color= primary_text_colour)
    }
}

@Composable
fun OtherTopPicksSectionListOfMatched(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ){
        Text("Other top picks", fontWeight = FontWeight.Bold, fontSize = 25.sp, color = primary_text_colour)
    }
}

@Composable
fun ListRestaurantsListOfMatched(size: Int, placesA: List<MealMatesPlace>){
    for(i in 1..<3){
        Column(
            modifier = Modifier
                .height(82.dp)
                .padding(vertical = 0.dp)
                .fillMaxWidth()
                .background(color = selectableList_colour)
                .clickable { /*to the corresponding group page*/ },

            ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                val PhotoVal = placesA[i].photos[0].photoReference
                val photoByteArrayA = fetchPlacePhoto(PhotoVal)
                Image(
                    bitmap = convertImageByteArrayToBitmap(photoByteArrayA!!).asImageBitmap(),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                    ,
                )
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 0.dp, horizontal = 20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(placesA[i].displayName, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(placesA[i].shortFormattedAddress, fontSize = 15.sp)
                }
            }


        }
        Divider(color = Color.White, thickness = 1.dp)
    }
}
@Composable
fun AllergenInfoMessageCard() {
    Card(
        backgroundColor = component_colour,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row {
                Icon(
                    Icons.Outlined.Info,
                    "Important information",
                    tint = primary_text_colour,
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
                Text(
                    text = "Important Information",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = primary_text_colour
                )
            }

            Text(
                text = "Currently, our app does not provide specific information on Accessibility, Allergens and Religious dietary restrictions due to data limitations. Please verify accessibility and dietary information directly with the restaurants. Thank you for understanding."
            )
        }
    }
}
