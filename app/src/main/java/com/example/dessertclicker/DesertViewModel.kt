package com.example.dessertclicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DesertUIState (
    val revenue: Int = 0,
    val dessertsSold: Int = 0,
    val currentDessertIndex: Int = 0,
    val currentDessertPrice: Int = dessertList[currentDessertIndex].price,
    val currentDessertImageId: Int = dessertList[currentDessertIndex].imageId)

class DesertViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(DesertUIState())

    val uiState: StateFlow<DesertUIState> = _uiState.asStateFlow()


    fun sell() {
        val revenue = _uiState.value.revenue.plus(_uiState.value.currentDessertPrice)
        val desertSold = _uiState.value.dessertsSold.inc()

        val dessertToShow = determineDessertToShow()

        _uiState.update { currentState ->
            currentState.copy(
                revenue = revenue,
                dessertsSold = desertSold,
                currentDessertImageId = dessertToShow.imageId,
                currentDessertPrice = dessertToShow.price

            )
        }
    }

    /**
     * Determine which dessert to show.
     */
    private fun determineDessertToShow(): Dessert {
        var dessertToShow = dessertList.first()
        for (dessert in dessertList) {
            if (_uiState.value.dessertsSold >= dessert.startProductionAmount) {
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

}