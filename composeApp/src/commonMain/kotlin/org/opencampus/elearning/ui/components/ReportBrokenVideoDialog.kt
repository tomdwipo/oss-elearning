package org.opencampus.elearning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.opencampus.elearning.domain.model.ReportReason
import org.opencampus.elearning.ui.theme.OpenCampusColors

@Composable
fun ReportBrokenVideoDialog(
    onDismissRequest: () -> Unit,
    onSubmitReport: (reason: ReportReason, notes: String) -> Unit
) {
    var selectedReason by remember { mutableStateOf(ReportReason.DELETED_OR_PRIVATE) }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(16.dp),
        containerColor = OpenCampusColors.GrayscaleWhite,
        title = {
            Column {
                Text(
                    text = "Laporkan Link Rusak",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OpenCampusColors.GrayscaleBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bantu kurator kami memperbarui materi belajar ini.",
                    fontSize = 13.sp,
                    color = OpenCampusColors.GrayscaleHintText
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Pilih Alasan Masalah:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OpenCampusColors.GrayscaleBlack,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                ReportReason.values().forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedReason = reason }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (reason == selectedReason),
                            onClick = { selectedReason = reason },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = OpenCampusColors.CorporatePurple,
                                unselectedColor = OpenCampusColors.GrayscaleBorder
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = reason.label,
                            fontSize = 14.sp,
                            color = OpenCampusColors.GrayscaleBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Catatan Tambahan (Opsional):",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OpenCampusColors.GrayscaleBlack,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = {
                        Text(
                            text = "Contoh: Video bermasalah di menit ke 05:20...",
                            fontSize = 12.sp,
                            color = OpenCampusColors.GrayscaleHintText
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    println("[ReportBrokenVideoDialog] Submitting report reason: ${selectedReason.name}, notes_length: ${notes.trim().length}")
                    onSubmitReport(selectedReason, notes.trim())
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = OpenCampusColors.CorporatePurple
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Kirim Laporan",
                    color = OpenCampusColors.GrayscaleWhite,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = "Batal",
                    color = OpenCampusColors.GrayscaleHintText,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}
