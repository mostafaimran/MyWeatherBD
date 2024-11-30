package com.my.weather.bd.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.my.weather.bd.R
import com.my.weather.bd.ui.viewmodel.WeatherBDViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: WeatherBDViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.search_zila)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Search Icon"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            val searchScreenState = viewModel.searchScreenState
            if (searchScreenState.loadZillaRequired) {
                viewModel.getZilaList()
            }

            val searchText = remember { mutableStateOf("") }

            val allZilaList = searchScreenState.zilaList ?: ArrayList()
            val filteredItems = allZilaList.filter { zila ->
                zila.name.contains(searchText.value, ignoreCase = true)
            }

            OutlinedTextField(
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                placeholder = { Text(stringResource(R.string.search_location)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )

            if (filteredItems.isNotEmpty())
                LazyColumn {
                    items(filteredItems.size) { index ->
                        Column(modifier = Modifier.clickable {
                            viewModel.getWeatherByLocation(
                                filteredItems[index].coord.lat,
                                filteredItems[index].coord.lon
                            )
                            onBack()
                        }) {
                            Spacer(Modifier.size(8.dp))
                            Text(filteredItems[index].name)
                            Spacer(Modifier.size(8.dp))
                            HorizontalDivider()
                        }
                    }
                }
        }
    }
}

