package br.com.prognum.gateway_certidao.core.utils;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import br.com.prognum.gateway_certidao.core.exceptions.InvalidDateException;

public class DateUtils {

	public static Instant fromScci(String dateAsString) throws InvalidDateException {
		try {
			String[] tokens = dateAsString.split("/");
			if (tokens.length != 3) {
				throw new InvalidDateException(String.format("Data inválida %s", dateAsString));
			}

			int dayOrMonth = Integer.parseInt(tokens[0]);
			int month = monthFromScci(tokens[1]);
			int year = Integer.parseInt(tokens[2]);
			
			ZonedDateTime zonedDateTime = ZonedDateTime.of(year, month, dayOrMonth, 0, 0, 0, 0, ZoneId.of("America/Sao_Paulo"));
			return zonedDateTime.toInstant();
		} catch (NumberFormatException e) {
			throw new InvalidDateException(e);
		} catch (DateTimeException e) {
			throw new InvalidDateException(e);
		}
	}

	private static int monthFromScci(String monthAsString) throws InvalidDateException {
		switch (monthAsString) {
		case "Jan":
			return 1;
		case "Fev":
			return 2;
		case "Mar":
			return 3;
		case "Abr":
			return 4;
		case "Mai":
			return 5;
		case "Jun":
			return 6;
		case "Jul":
			return 7;
		case "Ago":
			return 8;
		case "Set":
			return 9;
		case "Out":
			return 10;
		case "Nov":
			return 11;
		case "Dez":
			return 12;
		default:
			throw new InvalidDateException(String.format("Mês %s desconhecido", monthAsString));
		}
	}
}
