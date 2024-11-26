package com.my.weather.bd.ui.screen.views

/*

@Composable
fun PropertyMapScreen(
    viewModel: PropertyListViewModel,
    innerPadding: PaddingValues,
    modifier: Modifier,
    onNavigationBack: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val uiState = viewModel.uiState
    val properties = uiState.weatherResponse
    val isLoading = uiState.isLoading
    val exception = uiState.exception
    val currentLocation = uiState.currentLocation
    val requestLocation = uiState.locationRequired

    if (exception != null) {
        viewModel.exceptionHandled()
        viewModel.lvApiIssue.value = SingleEvent(exception)
    }

    var selectedProperty by remember { mutableStateOf<Property?>(null) }

    if (requestLocation) {
        LocationPermissionScreen(
            onLocationFound = { location ->
                viewModel.updateLocation(location)
            }, permissionDenied = {
                onPermissionDenied()
            }
        )
        return
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box {
            PropertyMap(
                modifier = Modifier.fillMaxSize(),
                properties = properties,
                currentLocation = currentLocation,
                onMarkerClick = { property -> selectedProperty = property },
                onFetchProperty = {
                    viewModel.getVisibleRadius(it)?.let { radius ->
                        if (currentLocation != null)
                            viewModel.getWeatherByLocation(
                                radius,
                                currentLocation.latitude,
                                currentLocation.longitude
                            )
                    }
                }
            )

            TopSheet(
                Modifier.align(Alignment.TopCenter),
                innerPadding.calculateTopPadding(),
                isLoading,
                viewModel,
                onNavigationBack
            )

            if (selectedProperty != null) {
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PropertyDetailCard(selectedProperty!!) {
                        selectedProperty = null
                    }
                    BookingInfoView()
                }
            }
        }
    }
}

@Composable
fun PropertyMap(
    modifier: Modifier,
    currentLocation: LatLng?,
    properties: List<Property>,
    onMarkerClick: (Property) -> Unit,
    onFetchProperty: (CameraPositionState) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        currentLocation?.let {
            position = CameraPosition.fromLatLngZoom(it, 5f)
        }
    }

    var selected by remember { mutableStateOf<Property?>(null) }
    var mapReady by remember { mutableStateOf(false) }
    var initialZoomReady by remember { mutableStateOf(false) }

    LaunchedEffect(initialZoomReady) {
        if (currentLocation != null && initialZoomReady) {
            onFetchProperty(cameraPositionState)
        }
    }

    LaunchedEffect(mapReady) {
        if (currentLocation != null) {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f))
        }

        initialZoomReady = true

    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            if (currentLocation != null && initialZoomReady)
                onFetchProperty(cameraPositionState)
        }
    }

    GoogleMap(
        modifier = modifier,
        properties = MapProperties(isMyLocationEnabled = true, isBuildingEnabled = true),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true, zoomControlsEnabled = false),
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            mapReady = true
        }
    ) {
        properties.forEach { property ->
            MarkerComposable(
                keys = arrayOf(property),
                state = MarkerState(
                    position = LatLng(
                        property.location.lat,
                        property.location.lng
                    )
                ),
                tag = property.id,
                onClick = {
                    selected = property
                    onMarkerClick(property)
                    true
                }
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (property == selected) Color.Red else Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (property == selected) Color.White else Color.Red
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                ) {
                    Text(
                        text = stringResource(R.string.taka_sign, property.price ?: 0),
                        fontSize = 14.sp,
                        color = if (selected == property) Color.White else Color.Red,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ImagePager(product: Property) {
    val items = product.images.map { it.url }

    val pagerState = rememberPagerState(pageCount = {
        items.size
    })

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(pagerState.currentPage) {
        if (!isDragged) {
            delay(3000)
            pagerState.scrollToPage((pagerState.currentPage + 1) % items.size)
        }
    }

    HorizontalPager(
        modifier = Modifier
            .size(120.dp),
        beyondViewportPageCount = 2,
        state = pagerState
    ) { page ->
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(items[page])
                .crossfade(true)
                .build(),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 12.dp, topStart = 12.dp))
        )
    }
}

@Composable
fun PropertyDetailCard(property: Property, onCancelDetail: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ImagePager(product = property)

                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Row {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = property.title ?: "",
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp
                            )

                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Gray),
                                modifier = Modifier.size(24.dp),
                                onClick = {
                                    onCancelDetail()
                                }
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.bdt_price, property.price ?: 0),
                                color = Color.Red,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row {
                                Icon(
                                    Icons.Default.Star,
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp),
                                    contentDescription = "Close"
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    fontWeight = FontWeight.Bold,
                                    text = property.reviewsAvg?.toString() ?: "0.0",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    fontWeight = FontWeight.Light,
                                    text = "(${(property.reviewsCount?.toString() ?: "0.0")})",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun TopSheet(
    modifier: Modifier,
    topPadding: Dp,
    loading: Boolean,
    viewModel: PropertyListViewModel,
    onNavigationBack: () -> Unit
) {
    val context = LocalContext.current

    var showLog by remember { mutableStateOf(false) }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.padding(
                top = topPadding,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Black),
                onClick = {
                    onNavigationBack()
                }
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color.Gray),
                onClick = {
                    showLog = true
                }
            ) {
                Icon(Icons.Default.Info, contentDescription = "log")
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.compare_similar_listing),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Entire Home/apt 2 bedrooms",
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 16.sp
                )

                if (loading) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AndroidView(factory = {
                        CustomLinearProgressBar(context = context)
                    }, modifier = Modifier.height(8.dp))
                }

            }
        }

        if (showLog) {
            LogSheetLayout(viewModel.logDataList) {
                showLog = false
            }
        }

    }
}

@Composable
fun BookingInfoView() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "Booked Properties",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Sep 20 - Nov 18",
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}*/
