package com.example.seldonfleet.ui.screens.detail

import androidx.lifecycle.ViewModel
import com.example.seldonfleet.domain.model.Device
import com.example.seldonfleet.domain.usecase.ClassifyDeviceUseCase

class DetailViewModel : ViewModel() {
    private val classify = ClassifyDeviceUseCase()
    fun classification(device: Device) = classify(device)
}
