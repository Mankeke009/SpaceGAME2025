package puppy.code;

import com.badlogic.gdx.math.Rectangle;

/**
 * GM1.5: Interfaz que define el comportamiento de colisión.
 * Aporta funcionalidad real permitiendo verificar choques y obtener áreas.
 */
public interface Colisionable {
    public boolean verificarColision(Colisionable otroObjeto);
    public Rectangle getArea();
}