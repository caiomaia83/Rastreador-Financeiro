package com.app.rastreadorfinanceiro.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object IconConverter {
    fun iconToString(icon: ImageVector): String {
        return when (icon) {
            Icons.Default.Home -> "Home"
            Icons.Default.ShoppingCart -> "ShoppingCart"
            Icons.Default.Place -> "Place"
            Icons.Default.Phone -> "Phone"
            Icons.Default.Call -> "Call"
            Icons.Default.Favorite -> "Favorite"
            Icons.Default.FavoriteBorder -> "FavoriteBorder"
            Icons.Default.Star -> "Star"
            Icons.Default.Settings -> "Settings"
            Icons.Default.Build -> "Build"
            Icons.Default.Person -> "Person"
            Icons.Default.AccountCircle -> "AccountCircle"
            Icons.Default.AccountBox -> "AccountBox"
            Icons.Default.DateRange -> "DateRange"
            Icons.Default.Info -> "Info"
            Icons.Default.MoreVert -> "MoreVert"
            Icons.Default.Email -> "Email"
            Icons.Default.Notifications -> "Notifications"
            Icons.Default.Lock -> "Lock"
            Icons.Default.Add -> "Add"
            Icons.Default.Edit -> "Edit"
            Icons.Default.Delete -> "Delete"
            Icons.Default.CheckCircle -> "CheckCircle"
            Icons.Default.Check -> "Check"
            else -> "Add"
        }
    }

    fun stringToIcon(iconName: String): ImageVector {
        return when (iconName) {
            "Home" -> Icons.Default.Home
            "ShoppingCart" -> Icons.Default.ShoppingCart
            "Place" -> Icons.Default.Place
            "Phone" -> Icons.Default.Phone
            "Call" -> Icons.Default.Call
            "Favorite" -> Icons.Default.Favorite
            "FavoriteBorder" -> Icons.Default.FavoriteBorder
            "Star" -> Icons.Default.Star
            "Settings" -> Icons.Default.Settings
            "Build" -> Icons.Default.Build
            "Person" -> Icons.Default.Person
            "AccountCircle" -> Icons.Default.AccountCircle
            "AccountBox" -> Icons.Default.AccountBox
            "DateRange" -> Icons.Default.DateRange
            "Info" -> Icons.Default.Info
            "MoreVert" -> Icons.Default.MoreVert
            "Email" -> Icons.Default.Email
            "Notifications" -> Icons.Default.Notifications
            "Lock" -> Icons.Default.Lock
            "Add" -> Icons.Default.Add
            "Edit" -> Icons.Default.Edit
            "Delete" -> Icons.Default.Delete
            "CheckCircle" -> Icons.Default.CheckCircle
            "Check" -> Icons.Default.Check
            else -> Icons.Default.Add
        }
    }
}
