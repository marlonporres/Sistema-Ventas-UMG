/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gt.edu.umg.sistema.estudiantes;

import gt.edu.umg.sistema.estudiantes.vista.FrmContenedorPadre;
import gt.edu.umg.sistema.estudiantes.controlador.ContenedorPadreController;

/**
 * @author marlo
 */
public class SistemaEstudiantes {

    public static void main(String[] args) {
        // Instancia la vista del contenedor MDI real que tienes en tu proyecto
        FrmContenedorPadre menu = new FrmContenedorPadre();
        
        // Asigna su controlador correspondiente
        ContenedorPadreController control = new ContenedorPadreController(menu);
        
        // Muestra el formulario padre en pantalla
        menu.setVisible(true);
    }
}