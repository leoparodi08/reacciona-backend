package ar.edu.udemm.reacciona.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Estudiante") //Valor que se guardará en la columna "user_type"
public class Estudiante extends Usuario {
    private int puntos;

    // Getters y Setters
    public int getPuntos() {
        return puntos;
    }
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
