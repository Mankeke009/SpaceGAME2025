/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package puppy.code;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

/*
  REQUISITO GM1.4: Clase abstracta que sirve como padre para todas las pantallas.
 
 */
public abstract class PantallaBase implements Screen {
    protected SpaceNavigation game;
    protected OrthographicCamera camera;

 
    public PantallaBase(SpaceNavigation game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, 1200, 800);
    }

    @Override
    public abstract void render(float delta); //REQUISITO GM1.4: Método abstracto.


    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
