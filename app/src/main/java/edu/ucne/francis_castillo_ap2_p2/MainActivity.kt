package edu.ucne.francis_castillo_ap2_p2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.francis_castillo_ap2_p2.Presentacion.gasto.GastoScreen
import edu.ucne.francis_castillo_ap2_p2.ui.theme.Francis_Castillo_AP2_P2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Francis_Castillo_AP2_P2Theme {
                GastoScreen()
            }
        }
    }
}
