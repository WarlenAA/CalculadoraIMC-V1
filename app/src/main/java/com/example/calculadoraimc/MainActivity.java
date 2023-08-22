package com.example.calculadoraimc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText peso;
    private EditText altura;
    private TextView resultado;
    private TextView imc;
    private TextView pesoIdeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // A variável resultado vai receber o valor do ID que passamos
        peso = findViewById(R.id.peso);
        altura = findViewById(R.id.altura);
        resultado = findViewById(R.id.resultado);
        imc = findViewById(R.id.imc);
        pesoIdeal = findViewById(R.id.pesoIdeal);

        // Definir filtros de entrada para peso e altura
        peso.setFilters(new InputFilter[]{new InputFilterMinMax(1, 250)});
        altura.setFilters(new InputFilter[]{new InputFilterMinMax(1, 250)});
    }

    //método para fazer os cálculos
    public void calcularImc(View view){

        double Peso = Double.parseDouble(peso.getText().toString().replace(",", ".")); //double é um número decimal e peso recebe as informações da caixa peso; paserDouble para converter para float
        //o replace substitui a "," por "." caso o usuário digite
        double Altura = Double.parseDouble(altura.getText().toString());

        //Verifica se o usuário digitou em Metros ou Cm
        String alturaString = altura.getText().toString();
        if (alturaString.contains(".")) {
            Altura = Double.parseDouble(alturaString);
        } else {
            double alturaCentimetros = Double.parseDouble(alturaString);
            Altura = alturaCentimetros / 100.0; // Convertendo centímetros para metros
        }
        // Fim verificação

        //Continua a declarar a variável
        double Resultado = Peso / (Altura * Altura); //fórmula do IMC; pego o mesmo nome das double Yaqui

        // Formatar o resultado do IMC com duas casas decimais
        @SuppressLint("DefaultLocale") String resultadoFormatado = String.format("%.2f", Resultado);
        imc.setText(resultadoFormatado);

        if(Resultado < 17) {
            resultado.setText(R.string.muito_abaixo_do_peso);
        }else if(Resultado > 17 && Resultado <= 18.49) {
            resultado.setText(R.string.abaixo_do_peso);
        }else if(Resultado >18.49 && Resultado <= 24.99){
            resultado.setText(R.string.peso_ideal_parabens);
            pesoIdeal.setText(R.string.seu_peso_esta_otimo_continue_assim);
        }else if(Resultado >24.99 && Resultado <= 29.99){
            resultado.setText(R.string.levemente_acima_do_peso);
        }else if(Resultado >29.99 && Resultado <= 34.99){
            resultado.setText(R.string.obesidade_grau_i);
        }else if(Resultado >34.99 && Resultado <= 39.99){
            resultado.setText(R.string.obesidade_grau_ii_severa);
        }else if(Resultado >39.99){
            resultado.setText(R.string.obesidade_grau_iii_m_bida);
        }else{
            resultado.setText(R.string.insira_os_dados_solicitados);
        }

        // Calcular peso ideal
        double pesoMinimo = 18.5 * (Altura * Altura);
        double pesoMaximo = 24.99 * (Altura * Altura);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String pesoMinimoFormatado = decimalFormat.format(pesoMinimo);
        String pesoMaximoFormatado = decimalFormat.format(pesoMaximo);

        if (Peso < pesoMinimo) {
            double pesoDiferenca = pesoMinimo - Peso;
            String pesoDiferencaFormatado = decimalFormat.format(pesoDiferenca);
            pesoIdeal.setText(getString(R.string.seu_peso_ideal_deve_estar_entre) + pesoMinimoFormatado + "kg e " + pesoMaximoFormatado + "kg. Você deve ganhar " + pesoDiferencaFormatado + "kg.");
        } else if (Peso > pesoMaximo) {
            double pesoDiferenca = Peso - pesoMaximo;
            String pesoDiferencaFormatado = decimalFormat.format(pesoDiferenca);
            pesoIdeal.setText("Seu peso ideal deve estar entre " + pesoMinimoFormatado + "kg e " + pesoMaximoFormatado + "kg. Você deve perder " + pesoDiferencaFormatado + "kg.");
        } else if (Peso >= pesoMinimo && Peso <= pesoMaximo) {
            pesoIdeal.setText("Você está no peso ideal, que é entre " + pesoMinimoFormatado + "kg e " + pesoMaximoFormatado + "kg.");
        }else {
            pesoIdeal.setText("Seu peso está ideal para sua altura!");
        }
    }

    public class InputFilterMinMax implements InputFilter {
        private final double min;
        private final double max;

        public InputFilterMinMax(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            try {
                String newVal = dest.toString().substring(0, dstart) +
                        source.toString().substring(start, end) +
                        dest.toString().substring(dend);

                double input = Double.parseDouble(newVal);
                if (isInRange(min, max, input)) {
                    return null; // A entrada está dentro do intervalo permitido
                }
            } catch (NumberFormatException ignored) {
            }
            return ""; // A entrada não está dentro do intervalo permitido
        }

        private boolean isInRange(double a, double b, double c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}

