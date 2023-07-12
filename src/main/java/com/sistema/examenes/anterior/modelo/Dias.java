package com.sistema.examenes.anterior.modelo;

import java.util.Calendar;
import java.util.Date;

public enum Dias {
	LUNES,MARTES,MIÉRCOLES,JUEVES,VIERNES,SÁBADO,DOMINGO;
	
	
	public int getEnumDiasDeDate(Date fecha) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
        int[] diasSemana = {Dias.DOMINGO.ordinal(), Dias.LUNES.ordinal(), Dias.MARTES.ordinal(),
        		Dias.MIÉRCOLES.ordinal(), Dias.JUEVES.ordinal(), Dias.VIERNES.ordinal(), Dias.SÁBADO.ordinal()};
        return diasSemana[diaSemana - 1];
	}
}
