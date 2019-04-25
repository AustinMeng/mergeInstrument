package org.austin.mergeInstruments.rule;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.austin.mergeInstruments.instrument.Instrument;
import org.austin.mergeInstruments.instrument.InstrumentContent;
import org.junit.Before;
import org.junit.Test;

public class MergedRuleTest {

	Rule rule;

	@Before
	public void initialize() {
		Rule.mapIns.put(Instrument.PRIME, Instrument.LME);
	}

	@Test
	public void publishOneAndMoreLME() throws IOException {
		rule = new MergedRule();
		StringBuilder content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |" + "\n");
		content.append("|  15-03-2018        | 17-03-2018    |  LME_PB   | Lead 13 March 2018 |");
		InstrumentContent insContent = new InstrumentContent(Instrument.LME, "PB_03_2018", content.toString(), true);

		StringBuilder result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | TRUE     |");
		assertEquals(result.toString(), rule.parse(insContent));

		content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |" + "\n");
		content.append("|  15-03-2018        | 17-03-2018    |  LME_PB   | Lead 13 March 2018 |");
		insContent = new InstrumentContent(Instrument.LME, "PB_03_2018", content.toString(), true);

		result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | TRUE     |");
	}

	@Test
	public void publishOneAndMorePrime() throws IOException {
		rule = new MergedRule();
		StringBuilder content = new StringBuilder();
		content.append(
				"|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | EXCHANGE_CODE | TRADABLE |"
						+ "\n");
		content.append(
				"|  14-03-2018        | 18-03-2018    |  LME_PB   | Lead 13 March 2018 | PB_03_2018    | FALSE    |");
		InstrumentContent insContent = new InstrumentContent(Instrument.PRIME, "PB_03_2018", content.toString(), false);

		StringBuilder result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  14-03-2018        | 18-03-2018    |      PB   | Lead 13 March 2018 | FALSE    |");
		assertEquals(result.toString(), rule.parse(insContent));

		content = new StringBuilder();
		content.append(
				"|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | EXCHANGE_CODE | TRADABLE |"
						+ "\n");
		content.append(
				"|  15-03-2018        | 17-03-2018    |  LME_PB   | Lead 13 March 2018 | PB_03_2018    | FALSE    |");
		insContent = new InstrumentContent(Instrument.PRIME, "PB_03_2018", content.toString(), false);

		result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | FALSE    |");
		assertEquals(result.toString(), rule.parse(insContent));
	}

	@Test
	public void publishLMEBeforePrime() throws IOException {
		rule = new MergedRule();

		// LME
		StringBuilder content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |" + "\n");
		content.append("|  15-03-2018        | 17-03-2018    |  LME_PB   | Lead 13 March 2018 |");
		InstrumentContent insContent = new InstrumentContent(Instrument.LME, "PB_03_2018", content.toString(), true);

		StringBuilder result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | TRUE     |");
		assertEquals(result.toString(), rule.parse(insContent));
		// Prime
		content = new StringBuilder();
		content.append(
				"|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | EXCHANGE_CODE | TRADABLE |"
						+ "\n");
		content.append(
				"|  10-03-2018        | 10-03-2018    |  LME_PB   | Lead 13 March 2018 | PB_03_2018    | FALSE    |");
		insContent = new InstrumentContent(Instrument.PRIME, "PB_03_2018", content.toString(), false);

		result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | FALSE    |");
		assertEquals(result.toString(), rule.parse(insContent));
	}
	
	@Test
	public void publishLMEWithDifferentExCodeBeforePrime() throws IOException {
		rule = new MergedRule();

		// LME PB_03_2018
		StringBuilder content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |" + "\n");
		content.append("|  15-03-2018        | 17-03-2018    |  LME_PB   | Lead 13 March 2018 |");
		InstrumentContent insContent = new InstrumentContent(Instrument.LME, "PB_03_2018", content.toString(), true);

		StringBuilder result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | TRUE     |");
		assertEquals(result.toString(), rule.parse(insContent));
		
		// LME PB_06_2018
		content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |" + "\n");
		content.append("|  15-06-2018        | 17-06-2018    |  LME_PB   | Lead 13 March 2018 |");
		insContent = new InstrumentContent(Instrument.LME, "PB_06_2018", content.toString(), true);

		result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-06-2018        | 17-06-2018    |      PB   | Lead 13 March 2018 | TRUE     |");
		assertEquals(result.toString(), rule.parse(insContent));
		// Prime PB_03_2018
		content = new StringBuilder();
		content.append(
				"|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | EXCHANGE_CODE | TRADABLE |"
						+ "\n");
		content.append(
				"|  10-03-2018        | 15-03-2018    |  LME_PB   | Lead 13 March 2018 | PB_03_2018    | FALSE    |");
		insContent = new InstrumentContent(Instrument.PRIME, "PB_03_2018", content.toString(), false);

		result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | FALSE    |");
		assertEquals(result.toString(), rule.parse(insContent));
		// Prime PB_06_2018
		content = new StringBuilder();
		content.append(
				"|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | EXCHANGE_CODE | TRADABLE |"
						+ "\n");
		content.append(
				"|  10-03-2018        | 15-03-2018    |  LME_PB   | Lead 13 March 2018 | PB_06_2018    | FALSE    |");
		insContent = new InstrumentContent(Instrument.PRIME, "PB_06_2018", content.toString(), false);

		result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-06-2018        | 17-06-2018    |      PB   | Lead 13 March 2018 | FALSE    |");
		assertEquals(result.toString(), rule.parse(insContent));
	}

	@Test
	public void publishLMEAfterPrime() throws IOException {
		rule = new MergedRule();

		// Prime
		StringBuilder content = new StringBuilder();
		content.append(
				"|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | EXCHANGE_CODE | TRADABLE |"
						+ "\n");
		content.append(
				"|  10-03-2018        | 18-03-2018    |  LME_PB   | Lead 13 March 2018 | PB_03_2018    | FALSE    |");
		InstrumentContent insContent = new InstrumentContent(Instrument.PRIME, "PB_03_2018", content.toString(), false);

		StringBuilder result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  10-03-2018        | 18-03-2018    |      PB   | Lead 13 March 2018 | FALSE    |");
		assertEquals(result.toString(), rule.parse(insContent));
		
		// LME
		content = new StringBuilder();
		content.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              |" + "\n");
		content.append("|  15-03-2018        | 17-03-2018    |  LME_PB   | Lead 13 March 2018 |");
		insContent = new InstrumentContent(Instrument.LME, "PB_03_2018", content.toString(), true);

		result = new StringBuilder();
		result.append("|  LAST_TRADING_DATE | DELIVERY_DATE |  MARKET   | LABEL              | TRADABLE |" + "\n");
		result.append("|  15-03-2018        | 17-03-2018    |      PB   | Lead 13 March 2018 | TRUE     |");
		assertEquals(result.toString(), rule.parse(insContent));
	}

}
