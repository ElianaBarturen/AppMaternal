package com.example.appmaternal.retrofit;

import com.example.appmaternal.model.Prediction;
import com.example.appmaternal.response.AntFamiliarListadoResponse;
import com.example.appmaternal.response.AntPersonalListadoResponse;
import com.example.appmaternal.response.DatosControlConsultarResponse;
import com.example.appmaternal.response.DatosControlConsultarResponsePrueba;
import com.example.appmaternal.response.GestanteActualizarResponse;
import com.example.appmaternal.response.GestanteAgregarResponse;
import com.example.appmaternal.response.GestanteConsultarResponse;
import com.example.appmaternal.response.GestanteDatosControlAgregarResponse;
import com.example.appmaternal.response.HistorialGestanteResponse;
import com.example.appmaternal.response.ListaGestanteObstetraResponse;
import com.example.appmaternal.response.LoginResponseMedico;
import com.example.appmaternal.response.ObsDatosControlReporterResponse;
import com.example.appmaternal.response.PredictionResponse;
import com.example.appmaternal.response.SemanasResponse;
import com.example.appmaternal.response.SintomasResponse;
import com.example.appmaternal.response.TipoEmbarazoListadoResponse;
import com.example.appmaternal.response.UsuariaConsultarResponse;
import com.example.appmaternal.response.UsuariaRegistrarResponse;
import com.example.appmaternal.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @FormUrlEncoded
    @POST("/login")
    Call<LoginResponse> login(@Field("email") String email, @Field("clave") String clave);

    @FormUrlEncoded
    @POST("/login/medico")
    Call<LoginResponseMedico> loginmedico(@Field("email") String email, @Field("clave") String clave);

    @FormUrlEncoded
    @POST("usuaria/insertar")
    Call<UsuariaRegistrarResponse> registrarUsuaria (@Field("nombres") String nombres, @Field("email") String email, @Field("clave") String contraseña);

    @GET("/antFamiliar/listar")
    Call<AntFamiliarListadoResponse> listarAntFamiliar();

    @GET("/antPersonal/listar")
    Call<AntPersonalListadoResponse> listarAntPersonal();

    @GET("/tipoEmbarazo/listar")
    Call<TipoEmbarazoListadoResponse> listarTipoEmbarazo();

    @FormUrlEncoded
    @POST("gestante/agregar")
    Call<GestanteAgregarResponse> agregarGestante(
            @Field("nombres") String nombres,
            @Field("dni") String dni,
            @Field("edad_gestante") int edad_gestante,
            @Field("email") String email,
            @Field("ant_familiar_id") int ant_familiar_id,
            @Field("ant_personal_id") int ant_personal_id,
            @Field("gestas") int gestas,
            @Field("tipo_embarazo_id") int tipo_embarazo_id,
            @Field("edad_gestacional") int edad_gestacional,
            @Field("usuaria_id") int usuaria_id);

    @FormUrlEncoded
    @POST("gestante/actualizar")
    Call<GestanteActualizarResponse> actualizarGestante(
            @Field("nombres") String nombres,
            @Field("dni") String dni,
            @Field("edad_gestante") int edad_gestante,
            @Field("email") String email,
            @Field("ant_familiar_id") int ant_familiar_id,
            @Field("ant_personal_id") int ant_personal_id,
            @Field("gestas") int gestas,
            @Field("tipo_embarazo_id") int tipo_embarazo_id,
            @Field("edad_gestacional") int edad_gestacional,
            @Field("id") int id);


    @GET("/gestante/datos/{id}")  //cuando es get va directo sin @FormUrl...
    Call<GestanteConsultarResponse> consultarGestante(@Path("id")int id);

    @GET("/gestante/datosControl/{usuaria_id}")  //cuando es get va directo sin @FormUrl...
    Call<GestanteConsultarResponse> consultarDatosGestante(@Path("usuaria_id")int usuaria_id);

    @FormUrlEncoded
    @POST("/datosControl/agregar")
    Call<GestanteDatosControlAgregarResponse> agregarDatosGestante(
            @Field("edad_gestante") int edad_gestante,
            @Field("edad_gestacional_sem") int edad_gestacional_sem,
            @Field("peso") float peso,
            @Field("pres_sistolica") int pres_sistolica,
            @Field("pres_diastolica") int pres_diastolica,
            @Field("frec_card") int frec_card,
            @Field("tipo_embarazo_id") int tipo_embarazo_id,
            @Field("gestas") int gestas,
            @Field("dolor_epigastrio") int dolor_epigastrio,
            @Field("VISION_BORROSA") int vision_borrosa,
            @Field("CEFALEA") int cefalea,
            @Field("nauseas") int nauseas,
            @Field("hinchazon_pies") int hinchazon_pies,
            @Field("ant_personal_id") int ant_personal_id,
            @Field("ant_familiar_id") int ant_familiar_id,
            @Field("diagnostico") int diagnostico,
            @Field("gestante_id") int gestante_id);


    @POST("/ws_prediccion/predict")
    Call<PredictionResponse> predict(@Body Prediction prediction);

    @GET("/datos/datosControl/{gestante_id}")
    Call<HistorialGestanteResponse> listarHistorial(@Path("gestante_id")int gestante_id);

    @GET("/usuaria/consultarIdGestante/{id}")
    Call<GestanteConsultarResponse> consultarIdGestante(@Path("id") int idUser);

    @GET("datos/datosControl/filtrar/{gestante_id}/{fecha_inicio}/{fecha_fin}")
    Call<HistorialGestanteResponse> filtrarHistorialFecha(
            @Path("gestante_id")int gestante_id,
            @Path("fecha_inicio")String fecha_inicio,
            @Path("fecha_fin")String fecha_fin);

    @GET("/datosControl/sintomas/{gestante_id}")
    Call<SintomasResponse> listarSintomas(@Path("gestante_id") int gestanteId);
    @GET("/datosControl/semanas/{gestante_id}")
    Call<SemanasResponse> listarSemanas(@Path("gestante_id") int gestanteId);
    @GET("/datosControl/semanaActual/{gestante_id}")
    Call<DatosControlConsultarResponse> listarSemanaActual(@Path("gestante_id") int gestanteId);

    @GET("/datosControl/reportesSemanas/{gestante_id}/{edad_gestacional_sem}")
    Call<DatosControlConsultarResponsePrueba> reporteSintomas(
            @Path("gestante_id") int gestanteId,
            @Path("edad_gestacional_sem") int edad_gestacional_sem);

    @GET("/gestante/listarGestanteObstetra")
    Call<ListaGestanteObstetraResponse> listarGestanteObstetra();

    @GET("/gestante/consultarDatosActualizar/{gestante_id}")  //cuando es get va directo sin @FormUrl...
    Call<GestanteConsultarResponse> consultarActalizarGestante(@Path("gestante_id")int gestante_id);
    @GET("gestante/consultarGestanteObstetraDni/{dni}")
    Call<ListaGestanteObstetraResponse> consultarGestanteObstetraDni(@Path("dni") String dni);

    @GET("/obstetra/reporteSintomasSemanaObs/{id}")
    Call<ObsDatosControlReporterResponse> reporteSintomasObs(@Path("id") Integer id);

}
