import java.io.FileInputStream;
import java.util.Properties;

import java.io.PrintStream; 
import java.io.FileOutputStream;

class StandardParser {
private PrintStream fileStream;

public StandardParser() {
		fileStream = new PrintStream(new FileOutputStream("src/main/resources/StandardParserLog.txt", true));
	}

	
	public String reverseString(String v_sMessage) {
        final String ROUTINENAME = "run";
		
		final int TERMINAL_ID_POS = 91;
		final int TERMINAL_ID_LEN = 11;
		final int STATUS_POS = 59;
		final int STATUS_LEN = 10;
		final int STATUS_DESC_POS = 71;
		final int STATUS_DESC_LEN = 15;
		final int DATE_POS = 1;
		final int DATE_LEN = 8;
		final int TIME_POS = 10;
		final int TIME_LEN = 8;
		
		final int MIN_MSG_LEN = 101;		//Minimum valid message length
		
		if(Len(v_sMessage) < MIN_MSG_LEN){
			LogIt(ROUTINENAME, "Invalid message: Message length is less than " + CStr(MIN_MSG_LEN) + " characters.");
			return null;
		}
		
		LogIt(ROUTINENAME, "Received: " + v_sMessage);
		
		String terminalId = Mid$(v_sMessage, TERMINAL_ID_POS, TERMINAL_ID_LEN).trim();
		String statusCode = Mid$(v_sMessage, STATUS_POS, STATUS_LEN).trim();
		String statusCodeDescription = Mid$(v_sMessage, STATUS_DESC_POS, STATUS_DESC_LEN).trim();
		String messageDate  = Mid$(v_sMessage, DATE_POS, DATE_LEN);
		String messageTime  = Mid$(v_sMessage, TIME_POS, TIME_LEN);
		
		//Change the date field from YY-MM-DD to YYYYMMDD
		messageDate = "20" + Mid$(messageDate, 1, 2) + Mid$(messageDate, 4, 2) + Mid$(messageDate, 7, 2);
				
		//Change the time field from HH:MM:SS to HHMMSS
		messageTime = Mid$(messageTime, 1, 2) + Mid$(messageTime, 4, 2) + Mid$(messageTime, 7, 2);
		
		String sMsgToSend = standardMessage(terminalId, "", messageDate, messageTime, statusCode, statusCodeDescription, "");
		
		fileStream.println(sMsgToSend);
		
    }

	private void LogIt(String routine, String message)
	{
		fileStream.println(message);
	}
	
	private String CStr(int input)
	{
		return String.valueOf(input);
	}
	
	private int Len(String input)
	{
		if(input == null) return 0;
		return input.length();
	}
	
	private String Mid$(String input, int start, int length)
	{
		return input.substring(start - 1, start + length - 1);
	}
	
	private String standardMessage(String terminalId, String processId, String date, String time, 
			String statusCode, String statusDescription, String qualifier){
		char fs = (char)28;
		char term = (char)10;
		StringBuffer sb = new StringBuffer()
		sb.append(terminalId + fs);
		sb.append(processId + fs);
		sb.append(date + fs); //YYYYMMDD
		sb.append(time + fs); //HHMMSS
		sb.append(statusCode + fs);
		sb.append(statusDescription + fs);
		sb.append(qualifier + fs);
		sb.append(term);
		return "(ST)" + fs + String.format("%03d", sb.length()) + fs + sb.toString();
	}
	

}