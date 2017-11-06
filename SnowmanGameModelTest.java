import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
public class SnowmanGameModelTest {
	
	private SnowmanGameModel model; 
	
	@Before
	public void setUp() throws Exception {
		model = new SnowmanGameModel();
	}
	
	@Test
	public void getGuessesWorks() {
		model.setGuesses(4);
		assertEquals(model.getGuesses(), 4);
	}
	
	@Test
	public void getLengthWorks() {
		model.setLength(4);
		assertEquals(model.getLength(), 4);
	}
	
	@Test
	public void DictionaryFillsUp(){ 
		model.initializeGameDictionary();
		assertFalse(model.getDictionaySize()!=0);
	}

	@Test
	public void GameIsRunning(){
		model.setGuesses(4);
		model.run();
		assertTrue(model.getGuesses() <= 4);
	}
}
