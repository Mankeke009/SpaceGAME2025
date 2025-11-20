package puppy.code;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;


public class PantallaMenu extends PantallaBase {//extends PantallaBase es el GM 1.4

	public PantallaMenu(SpaceNavigation game) {
                super(game);
	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0, 0, 1);

		camera.update();
		game.getBatch().setProjectionMatrix(camera.combined);

		game.getBatch().begin();
		
		// 1. Título (Más arriba)
		game.getFont().draw(game.getBatch(), "Bienvenido a Space Navigation !", 140, 700);
		
		// 2. HighScore (Debajo del título)
		game.getFont().draw(game.getBatch(), "HighScore Actual: " + HighScore.getInstance().getHighScore(), 140, 650);
		
		// 3. Sección de Controles (En el centro)
		game.getFont().draw(game.getBatch(), "--- CONTROLES ---", 140, 550);
		game.getFont().draw(game.getBatch(), "Mover: W A S D", 140, 500);
		game.getFont().draw(game.getBatch(), "Apuntar: Mouse", 140, 450);
		game.getFont().draw(game.getBatch(), "Disparar: Click Izquierdo / Espacio", 140, 400); // Coincide con el código
		game.getFont().draw(game.getBatch(), "Pausa: ESC", 140, 350);
		
		// 4. Instrucción de inicio (Abajo)
		game.getFont().draw(game.getBatch(), "Pincha en cualquier lado o presiona cualquier tecla para comenzar ...", 50, 200);
	
		game.getBatch().end();

		if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
			Screen ss = new PantallaJuego(game,1,3,0,1,1,10);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		}
	}
	
	
	@Override
	public void show() {		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
   
}