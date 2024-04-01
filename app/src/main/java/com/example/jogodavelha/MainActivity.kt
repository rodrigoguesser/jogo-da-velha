package com.example.jogodavelha

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jogodavelha.ui.theme.JogoDaVelhaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JogoDaVelhaTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var vitorias by remember {
        mutableIntStateOf(0)
    }
    var empates by remember {
        mutableIntStateOf(0)
    }
    var derrotas by remember {
        mutableIntStateOf(0)
    }
    var opcaoJogador by remember {
        mutableStateOf("")
    }
    var jogadorAtual by remember {
        mutableIntStateOf(0)
    }
    var contagemOpcaoInvalida by remember {
        mutableStateOf(false)
    }

    val tabuleiro = remember {
        mutableStateListOf(
            mutableListOf("", "", ""),
            mutableListOf("", "", ""),
            mutableListOf("", "", "")
        )
    }
    val alterarContagemOpcaoInvalida: () -> Unit = {
        contagemOpcaoInvalida = contagemOpcaoInvalida != true
    }

    val clicarJogador: () -> String = {
        contagemOpcaoInvalida = false

        opcaoJogador = if (jogadorAtual == 0) "X" else "O"

        jogadorAtual = if (jogadorAtual == 0) 1 else 0

        opcaoJogador
    }

    fun verificarVitoria(tabuleiro: List<List<String>>, opcaoJogador: String): Boolean {

        for (linha in tabuleiro) {
            if (linha.all { it == opcaoJogador }) {
                return true
            }
        }

        for (i in tabuleiro.indices) {
            if (tabuleiro.all { it[i] == opcaoJogador }) {
                return true
            }
        }

        return (tabuleiro[0][0] == opcaoJogador && tabuleiro[1][1] == opcaoJogador && tabuleiro[2][2] == opcaoJogador) ||
                (tabuleiro[0][2] == opcaoJogador && tabuleiro[1][1] == opcaoJogador && tabuleiro[2][0] == opcaoJogador)
    }
    fun verificarEmpate(tabuleiro: List<List<String>>): Boolean {
        for (linha in tabuleiro) {
            for (elemento in linha) {
                if (elemento.isEmpty()) {

                    return false
                }
            }
        }

        return true
    }
    fun resetarTabuleiro(tabuleiro: MutableList<MutableList<String>>) {
        for (linha in tabuleiro) {
            for (indice in linha.indices) {
                linha[indice] = ""
            }
        }
        opcaoJogador = ""
        jogadorAtual = 0
        contagemOpcaoInvalida = false
    }


    if (verificarVitoria(tabuleiro, opcaoJogador)) {
        if (opcaoJogador == "X") {
            vitorias++
            resetarTabuleiro(tabuleiro)
        } else if (opcaoJogador == "O") {
            derrotas++
            resetarTabuleiro(tabuleiro)
        }
    } else if(verificarEmpate(tabuleiro)) {
        empates++
        resetarTabuleiro(tabuleiro)
    }
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(
                id = R.drawable.fundo
            ), contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        QuadroJogar(jogadorAtual)
        TabuleiroJogo(tabuleiro = tabuleiro, alterarContagemOpcaoInvalida = alterarContagemOpcaoInvalida, clicarJogador = clicarJogador)
        Placar(vitorias = vitorias, empates = empates, derrotas = derrotas)

        if (contagemOpcaoInvalida) {
            OpcaoInvalida(jogadorAtual)
        }

    }

}

@Composable
fun Placar(vitorias: Int, empates: Int, derrotas: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Placar",
            color = Color.Gray,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Jogador 1:  $vitorias",
                    color = Color.Green,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Jogador 2:  $derrotas",
                    color = Color(0xFFFFA500),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = "Empates: $empates",
            color = Color.Red,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun QuadroJogar(jogadorAtual: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "Jogada do jogador:  ${jogadorAtual + 1} ",
            color = if (jogadorAtual == 0) Color.Green else Color(0xFFFFA500),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Composable
fun OpcaoInvalida(jogadorAtual: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = "Jogada inválida! Por favor, escolha novamente.",
            color = if (jogadorAtual == 0) Color.Green else Color(0xFFFFA500),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TabuleiroJogo(
    tabuleiro: List<MutableList<String>>,
    alterarContagemOpcaoInvalida: () -> Unit,
    clicarJogador: () -> String,
) {
    Column(modifier = Modifier.padding(0.dp, 16.dp)) {
        for (i in 0 until 3) {
            Row(modifier = Modifier) {
                for (j in 0 until 3) {
                    val maxWidth = when (j) {
                        0 -> 0.33f
                        1 -> 0.50f
                        else -> 1f
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(maxWidth)
                            .border(
                                border = BorderStroke(
                                    color = Color.Black,
                                    width = 1.dp
                                )
                            )
                            .clickable {
                                if (tabuleiro[i][j] == "") {
                                    tabuleiro[i][j] = clicarJogador()
                                } else {
                                    alterarContagemOpcaoInvalida()
                                }
                            },
                    ) {
                        Text(
                            text = tabuleiro[i][j],
                            fontSize = 90.sp,
                            color = if (tabuleiro[i][j] == "X") Color.Green else if (tabuleiro[i][j] == "O") Color(0xFFFFA500) else Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JogoDaVelhaTheme {
        MainScreen()
    }
}