package com.example.appmaternal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.compose.ui.text.Paragraph;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appmaternal.model.Gestante;
import com.example.appmaternal.model.ObsDatosControlGestanteReporte;
import com.example.appmaternal.response.ObsDatosControlReporterResponse;
import com.example.appmaternal.retrofit.ApiService;
import com.example.appmaternal.retrofit.RetrofitClient;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;



import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReporteObsSintomasActivity extends AppCompatActivity {
    private LineChart lineChartCefalea;
    private LineChart lineChartDolorEpigastrio;
    private LineChart lineChartHinchazonPies;
    private LineChart lineChartVisionBorrosa;
    private LineChart lineChartNauseas;
    String nombreGestante, nombreObstetra;

    MaterialButton btnPdf, prev;
    EditText editTextNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_obs_sintomas);

        // Inicializa los LineChart
        lineChartCefalea = findViewById(R.id.lineChartCefalea);
        lineChartDolorEpigastrio = findViewById(R.id.lineChartDolorEpigastrio);
        lineChartHinchazonPies = findViewById(R.id.lineChartHinchazonPies);
        lineChartVisionBorrosa = findViewById(R.id.lineChartVisionBorrosa);
        lineChartNauseas = findViewById(R.id.lineChartNauseas);

        btnPdf = findViewById(R.id.btnPdf);
        editTextNotes = findViewById(R.id.editTextNotes);
        //prev = findViewById(R.id.prev);

        lineChartCefalea.setNoDataText("No hay datos disponibles para Cefalea");
        lineChartCefalea.setNoDataTextColor(Color.RED); // Cambia el color del texto
        lineChartCefalea.invalidate(); // Refresca el gráfico

        lineChartDolorEpigastrio.setNoDataText("No hay datos disponibles para Dolor epigastrio");
        lineChartDolorEpigastrio.setNoDataTextColor(Color.RED); // Cambia el color del texto
        lineChartDolorEpigastrio.invalidate(); // Refresca el gráfico

        lineChartHinchazonPies.setNoDataText("No hay datos disponibles para Hinchazón de pies");
        lineChartHinchazonPies.setNoDataTextColor(Color.RED); // Cambia el color del texto
        lineChartHinchazonPies.invalidate(); // Refresca el gráfico

        lineChartVisionBorrosa.setNoDataText("No hay datos disponibles para Visión borrosa");
        lineChartVisionBorrosa.setNoDataTextColor(Color.RED); // Cambia el color del texto
        lineChartVisionBorrosa.invalidate(); // Refresca el gráfico

        lineChartNauseas.setNoDataText("No hay datos disponibles para Náuseas");
        lineChartNauseas.setNoDataTextColor(Color.RED); // Cambia el color del texto
        lineChartNauseas.invalidate(); // Refresca el gráfico


        // Recuperar el ID pasado desde la actividad anterior
        int gestanteId = getIntent().getIntExtra("ID_GESTANTE", -1);
        nombreGestante = getIntent().getStringExtra("NOMBRE_GESTANTE");
        Log.e("NOMBRE_GESTANTE recibido", String.valueOf(nombreGestante));
        Log.e("idgest_reporteobs", String.valueOf(gestanteId));

        if (gestanteId != -1) {
            listar(gestanteId);
        } else {
            Log.e("ReporteObsSintomasActivity", "ID_GESTANTE no recibido.");
        }

        MaterialButton btnGeneratePdf = findViewById(R.id.btnPdf);
        btnGeneratePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePDF();
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("MedicoSession", Context.MODE_PRIVATE);
         nombreObstetra = sharedPreferences.getString("NOMBRE_OBSTETRA", "");  // "" es el valor por defecto si no encuentra nada
        Log.e("nombre obs", nombreObstetra);


    }

    private void listar(int gestanteId) {
        ApiService apiService = RetrofitClient.createService();
        Call<ObsDatosControlReporterResponse> call = apiService.reporteSintomasObs(gestanteId);

        call.enqueue(new Callback<ObsDatosControlReporterResponse>() {
            @Override
            public void onResponse(final Call<ObsDatosControlReporterResponse> call, final Response<ObsDatosControlReporterResponse> response) {
                if (response.code() == 200 && response.body() != null && response.body().isStatus()) {
                    List<ObsDatosControlGestanteReporte> datos = response.body().getData();

                    if (datos != null && !datos.isEmpty()) {
                        configurarGraficos(datos);
                    } else {
                        Log.e("Error", "No hay datos disponibles.");
                    }
                } else {
                    Log.e("Error al acceder al servicio web", response.message());
                }
            }

            @Override
            public void onFailure(final Call<ObsDatosControlReporterResponse> call, final Throwable t) {
                Log.e("Falla al conectarse al servicio web", t.getMessage());
            }
        });
    }

    private void configurarGrafico(LineChart lineChart, List<ObsDatosControlGestanteReporte> datos, String tipoSintoma) {
        ArrayList<Entry> valores = new ArrayList<>();
        ArrayList<String> semanas = new ArrayList<>();

        // Extrae los datos y utiliza 'edad_gestacional_sem' para la semana
        for (ObsDatosControlGestanteReporte dato : datos) {
            int semana = dato.getEdad_gestacional_sem(); // Asegúrate de obtener la semana correcta
            float valorSintoma;

            // Asigna el valor del síntoma según el tipo
            switch (tipoSintoma) {
                case "Cefalea":
                    valorSintoma = dato.getCefalea();
                    break;
                case "Dolor Epigastrio":
                    valorSintoma = dato.getDolor_epigastrio();
                    break;
                case "Hinchazón Pies":
                    valorSintoma = dato.getHinchazon_pies();
                    break;
                case "Visión Borrosa":
                    valorSintoma = dato.getVision_borrosa();
                    break;
                case "Náuseas":
                    valorSintoma = dato.getNauseas();
                    break;
                default:
                    valorSintoma = 0;
            }

            // Agrega el valor de la semana como entrada al gráfico
            valores.add(new Entry(semanas.size(), valorSintoma)); // Aquí 'semanas.size()' da un índice secuencial para el gráfico

            // Agrega la semana como etiqueta del eje X
            semanas.add(String.valueOf(semana)); // Asegura que solo haya semanas únicas y las agregue como etiquetas
        }

        // Crear y configurar el dataset
        LineDataSet dataSet = new LineDataSet(valores, tipoSintoma);
        dataSet.setColor(Color.RED);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(true);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setLineWidth(5f);

        // Crear el LineData y configurar el gráfico
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        // Configura el eje X con las semanas
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= 0 && index < semanas.size()) {
                    return semanas.get(index) + " sem"; // Añadir "sem" al final
                } else {
                    return ""; // Devuelve una cadena vacía si el índice no es válido
                }
            }
        });

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Asegura que cada semana tenga una marca
        //xAxis.setLabelRotationAngle(-45); // Rotar etiquetas si es necesario para mejor visibilidad
        xAxis.setAvoidFirstLastClipping(true); // Asegura que no se corte la primera y última etiqueta

        // Configura el eje Y
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setGranularity(1f);
        yAxisLeft.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.0f veces", value);
            }
        });
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);

        // Configura el gráfico
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getDescription().setEnabled(false);

        lineChart.invalidate(); // Refresca el gráfico
    }
    private void configurarGraficos(List<ObsDatosControlGestanteReporte> datos) {
        configurarGrafico(lineChartCefalea, datos, "Cefalea");
        configurarGrafico(lineChartDolorEpigastrio, datos, "Dolor Epigastrio");
        configurarGrafico(lineChartHinchazonPies, datos, "Hinchazón Pies");
        configurarGrafico(lineChartVisionBorrosa, datos, "Visión Borrosa");
        configurarGrafico(lineChartNauseas, datos, "Náuseas");
    }

    private Bitmap getChartBitmap(LineChart chart) {
        chart.setDrawingCacheEnabled(true);
        chart.buildDrawingCache();
        return Bitmap.createBitmap(chart.getDrawingCache());
    }
    private static final int CREATE_FILE_REQUEST_CODE = 1;

    private void generatePDF() {
        // Iniciar el proceso para que el usuario seleccione una ubicación
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE, nombreGestante+"_ReporteSintomas.pdf");
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);
    }

    // Método para manejar el resultado de la selección de ubicación
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                // Generar y guardar el PDF en la ubicación seleccionada
                createPDFFile(uri);
            }
        }
    }


    private void createPDFFile(Uri uri) {
        try {
            // Obtener el texto de las notas ingresadas
            EditText editTextNotes = findViewById(R.id.editTextNotes);
            String userNotes = editTextNotes.getText().toString();

            // Obtener la fecha actual para el encabezado
            String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            // Crear el documento PDF
            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();

            // Crear la primera página para los gráficos
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();  // Tamaño A4
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            // Dibujar el encabezado en líneas separadas
            paint.setTextSize(18);
            canvas.drawText("Reporte de Síntomas: " + nombreGestante, 80, 50, paint);   // Título del reporte
            canvas.drawText("Fecha: " + currentDate, 80, 80, paint);  // Fecha actual
            canvas.drawText("Obstetra: " + nombreObstetra, 80, 110, paint);  // Nombre de la obstetra

            // Definir el tamaño de los gráficos
            int chartWidth = 500;
            int chartHeight = 300;

            // Dibujar el gráfico de Cefalea
            Bitmap cefaleaBitmap = getChartBitmap(findViewById(R.id.lineChartCefalea));
            Bitmap resizedCefaleaBitmap = Bitmap.createScaledBitmap(cefaleaBitmap, chartWidth, chartHeight, false);
            canvas.drawBitmap(resizedCefaleaBitmap, 50, 150, paint);  // Y = 150

            // Dibujar el gráfico de Dolor Epigastrio
            Bitmap dolorEpigastrioBitmap = getChartBitmap(findViewById(R.id.lineChartDolorEpigastrio));
            Bitmap resizedDolorEpigastrioBitmap = Bitmap.createScaledBitmap(dolorEpigastrioBitmap, chartWidth, chartHeight, false);
            canvas.drawBitmap(resizedDolorEpigastrioBitmap, 50, 470, paint);  // Y = 470

            // Finalizar la primera página
            pdfDocument.finishPage(page);

            // Crear la segunda página para más gráficos
            PdfDocument.PageInfo pageInfo2 = new PdfDocument.PageInfo.Builder(595, 842, 2).create();
            PdfDocument.Page page2 = pdfDocument.startPage(pageInfo2);
            Canvas canvas2 = page2.getCanvas();

            // Dibujar el gráfico de Hinchazón Pies
            Bitmap hinchazonPiesBitmap = getChartBitmap(findViewById(R.id.lineChartHinchazonPies));
            Bitmap resizedHinchazonPiesBitmap = Bitmap.createScaledBitmap(hinchazonPiesBitmap, chartWidth, chartHeight, false);
            canvas2.drawBitmap(resizedHinchazonPiesBitmap, 50, 150, paint);

            // Dibujar el gráfico de Visión Borrosa
            Bitmap visionBorrosaBitmap = getChartBitmap(findViewById(R.id.lineChartVisionBorrosa));
            Bitmap resizedVisionBorrosaBitmap = Bitmap.createScaledBitmap(visionBorrosaBitmap, chartWidth, chartHeight, false);
            canvas2.drawBitmap(resizedVisionBorrosaBitmap, 50, 470, paint);

            // Finalizar la segunda página
            pdfDocument.finishPage(page2);

            // Crear la tercera página para las notas y la firma
            PdfDocument.PageInfo pageInfo3 = new PdfDocument.PageInfo.Builder(595, 842, 3).create();
            PdfDocument.Page page3 = pdfDocument.startPage(pageInfo3);
            Canvas canvas3 = page3.getCanvas();

            // Dibujar las notas ingresadas por el usuario con título
            paint.setTextSize(24);
            canvas3.drawText("Notas adicionales por la obstetra:", 50, 100, paint);  // Empezar desde Y = 100
            paint.setTextSize(20);

            // Dividir el texto de notas en varias líneas si es muy largo
            int yPos = 150;  // Ajustar la posición inicial para las notas
            for (String line : userNotes.split("\n")) {
                canvas3.drawText(line, 50, yPos, paint);
                yPos += 30;
            }

            // Dibujar la firma en el pie de página
            Bitmap firmaBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.firma);  // Cargar la firma desde los recursos
            Bitmap resizedFirmaBitmap = Bitmap.createScaledBitmap(firmaBitmap, 200, 100, false);  // Redimensionar la firma
            canvas3.drawBitmap(resizedFirmaBitmap, 195, 720, paint);  // Posicionar la firma en el pie de página

            // Dibujar el texto "Firma" debajo de la imagen
            paint.setTextSize(18);
            canvas3.drawText("Firma", 275, 830, paint);

            // Finalizar la tercera página
            pdfDocument.finishPage(page3);

            // Guardar el PDF en archivo
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            pdfDocument.writeTo(outputStream);
            pdfDocument.close();
            outputStream.close();

            Toast.makeText(this, "PDF generado y guardado correctamente", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
