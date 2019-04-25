package org.austin.mergeInstruments.instrument;

public class LMEInstrument implements Instrument {

	@Override
	public InstrumentContent publish(){
		StringBuilder content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |"+"\n");
		content.append("|  15-03-2018        | 17-03-2018    |  LME_PB   | Lead 13 March 2018 |");
		InstrumentContent insContent = new InstrumentContent(LME, "PB_03_2018", content.toString(), true);
		return insContent;
	}

}
