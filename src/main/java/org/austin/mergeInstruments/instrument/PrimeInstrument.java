package org.austin.mergeInstruments.instrument;

public class PrimeInstrument implements Instrument {

	@Override
	public InstrumentContent publish(){
		StringBuilder content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | EXCHANGE_CODE | TRADABLE |"+"\n");
		content.append("|  14-03-2018        | 18-03-2018    |  LME_PB   | Lead 13 March 2018 | PB_03_2018    | FALSE    |");
		InstrumentContent insContent = new InstrumentContent(PRIME, "PB_03_2018", content.toString(), false);
		return insContent;
	}

}
