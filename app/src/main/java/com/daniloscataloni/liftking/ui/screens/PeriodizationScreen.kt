package com.daniloscataloni.liftking.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniloscataloni.liftking.entities.Periodization
import com.daniloscataloni.liftking.ui.components.DialogButtonRow
import com.daniloscataloni.liftking.ui.components.LiftKingHeading
import com.daniloscataloni.liftking.ui.components.LiftKingTextField
import com.daniloscataloni.liftking.ui.components.MediumSpacer
import com.daniloscataloni.liftking.ui.utils.BackgroundGray
import com.daniloscataloni.liftking.ui.utils.BorderGray
import com.daniloscataloni.liftking.ui.viewmodels.PeriodizationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun PeriodizationScreen(
    viewModel: PeriodizationViewModel = koinViewModel(),
    onPeriodizationSelected: (Periodization) -> Unit
) {
    val periodizations by viewModel.periodizations.collectAsState()
    val activePeriodization by viewModel.activePeriodization.collectAsState()
    val showDialog by viewModel.showCreateDialog.collectAsState()
    val newName by viewModel.newPeriodizationName.collectAsState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Text(
                modifier = Modifier.padding(top = 48.dp, start = 16.dp, bottom = 16.dp),
                text = "Periodizações",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onShowCreateDialog() },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nova periodização",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { paddingValues ->

        if (showDialog) {
            CreatePeriodizationDialog(
                name = newName,
                onNameChange = { viewModel.onNameChange(it) },
                onDismiss = { viewModel.onDismissCreateDialog() },
                onConfirm = { viewModel.createPeriodization() }
            )
        }

        if (periodizations.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Nenhuma periodização criada",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                MediumSpacer()
                Text(
                    text = "Toque no + para criar uma nova",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(periodizations) { periodization ->
                    PeriodizationCard(
                        periodization = periodization,
                        isActive = periodization.id == activePeriodization?.id,
                        onClick = {
                            viewModel.setActive(periodization)
                            onPeriodizationSelected(periodization)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PeriodizationCard(
    periodization: Periodization,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else BackgroundGray
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isActive) MaterialTheme.colorScheme.primary else BorderGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = periodization.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Ativa",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreatePeriodizationDialog(
    name: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LiftKingHeading(text = "Nova Periodização")
                MediumSpacer()
                LiftKingTextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = "Ex: Hipertrofia 2024"
                )
                MediumSpacer()
                DialogButtonRow(
                    onCancel = onDismiss,
                    onConfirm = { if (name.isNotBlank()) onConfirm() }
                )
            }
        }
    }
}
