/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoescuela1.Datos;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import proyectoescuela1.Controlador.*;
import proyectoescuela1.Modelo.*;

/**
 * Clase que carga datos de prueba en el sistema. Llama a cargarTodo() al
 * iniciar el sistema para tener datos listos para probar.
 *
 * IMPORTANTE: Solo usar en modo prueba. Comentar o borrar antes de entregar.
 */
public class DatosPrueba {

    // ── CONTROLADORES ─────────────────────────────
    private AlumnoControlador alumnoCtrl
            = new AlumnoControlador();
    private ApoderadoControlador apoderadoCtrl
            = new ApoderadoControlador();
    private DocenteControlador docenteCtrl
            = new DocenteControlador();
    private CursoControlador cursoCtrl
            = new CursoControlador();
    private HorarioControlador horarioCtrl
            = new HorarioControlador();
    private AsignacionCursoControlador asigCtrl
            = new AsignacionCursoControlador();
    private NotaControlador notaCtrl
            = new NotaControlador();
    private AsistenciaControlador regAsisCtrl
            = new AsistenciaControlador();
    private PeriodoControlador periodoCtrl
            = new PeriodoControlador();

    // ── MÉTODO PRINCIPAL ──────────────────────────
    /**
     * Carga todos los datos de prueba. Solo carga si no hay datos previos para
     * no duplicar.
     */
    public void cargarTodo() {
        System.out.println("=== CARGANDO DATOS "
                + "DE PRUEBA ===");

        // verifica si ya hay datos
        if (!alumnoCtrl.listarTodos().isEmpty()) {
            System.out.println(
                    "Ya existen datos. "
                    + "Omitiendo carga de prueba."
            );
            return;
        }

        cargarDocentes();
        cargarCursos();
        cargarHorarios();
        cargarAlumnos();
        cargarApoderados();
        cargarPeriodoAcademico();
        cargarNotas();
        cargarAsistencias();

        System.out.println("=== DATOS DE PRUEBA "
                + "CARGADOS ===");
    }

    // ══════════════════════════════════════════════
    //  DOCENTES
    // ══════════════════════════════════════════════
    private void cargarDocentes() {
        System.out.println("Cargando docentes...");

        docenteCtrl.registrarDocente(new Docente(
                1, "María", "García López",
                "45123456", "mgarcia@cole.pe",
                "987111222", "Av. Lima 123",
                new Date(), "Matemática",
                "Secundaria", new Date()
        ));

        docenteCtrl.registrarDocente(new Docente(
                2, "Carlos", "Rodríguez Pérez",
                "45234567", "crodriguez@cole.pe",
                "987222333", "Av. Arequipa 456",
                new Date(), "Comunicación",
                "Secundaria", new Date()
        ));

        docenteCtrl.registrarDocente(new Docente(
                3, "Ana", "Torres Quispe",
                "45345678", "atorres@cole.pe",
                "987333444", "Jr. Cusco 789",
                new Date(), "Ciencias",
                "Secundaria", new Date()
        ));

        docenteCtrl.registrarDocente(new Docente(
                4, "Luis", "Mamani Flores",
                "45456789", "lmamani@cole.pe",
                "987444555", "Calle Real 321",
                new Date(), "Historia",
                "Secundaria", new Date()
        ));

        docenteCtrl.registrarDocente(new Docente(
                5, "Rosa", "Vargas Huanca",
                "45567890", "rvargas@cole.pe",
                "987555666", "Av. Perú 654",
                new Date(), "Primaria",
                "Primaria", new Date()
        ));

        docenteCtrl.registrarDocente(new Docente(
                6, "Pedro", "Sánchez Díaz",
                "45678901", "psanchez@cole.pe",
                "987666777", "Jr. Bolívar 987",
                new Date(), "Educación Física",
                "Primaria", new Date()
        ));

        docenteCtrl.registrarDocente(new Docente(
                7, "Julia", "Medina Cruz",
                "45789012", "jmedina@cole.pe",
                "987777888", "Av. Colonial 147",
                new Date(), "Computación",
                "Primaria", new Date()
        ));

        System.out.println("✓ "
                + docenteCtrl.totalDocentes()
                + " docentes cargados.");
    }

    // ══════════════════════════════════════════════
    //  CURSOS
    // ══════════════════════════════════════════════
    private void cargarCursos() {
        System.out.println("Cargando cursos...");

        // cursos de secundaria
        cursoCtrl.registrar(new Curso(
                "Matemática", "Ciencias", 5, "Secundaria"
        ));
        cursoCtrl.registrar(new Curso(
                "Comunicación", "Letras", 5, "Secundaria"
        ));
        cursoCtrl.registrar(new Curso(
                "Ciencias", "Ciencias", 4, "Secundaria"
        ));
        cursoCtrl.registrar(new Curso(
                "Historia", "Letras", 3, "Secundaria"
        ));
        cursoCtrl.registrar(new Curso(
                "Inglés", "Letras", 3, "Secundaria"
        ));

        // cursos de primaria
        cursoCtrl.registrar(new Curso(
                "Matemática", "Ciencias", 5, "Primaria"
        ));
        cursoCtrl.registrar(new Curso(
                "Comunicación", "Letras", 5, "Primaria"
        ));
        cursoCtrl.registrar(new Curso(
                "Educación Física", "Deportes",
                2, "Primaria"
        ));
        cursoCtrl.registrar(new Curso(
                "Computación", "Tecnología", 2, "Primaria"
        ));

        System.out.println("✓ "
                + cursoCtrl.total()
                + " cursos cargados.");
    }

    // ══════════════════════════════════════════════
    //  HORARIOS
    // ══════════════════════════════════════════════
    private void cargarHorarios() {
        System.out.println("Cargando horarios...");

        horarioCtrl.registrar(new Horario(
                "Lunes", "08:00", "09:30",
                "Regular", "Secundaria"
        ));
        horarioCtrl.registrar(new Horario(
                "Lunes", "09:30", "11:00",
                "Regular", "Secundaria"
        ));
        horarioCtrl.registrar(new Horario(
                "Martes", "08:00", "09:30",
                "Regular", "Secundaria"
        ));
        horarioCtrl.registrar(new Horario(
                "Miércoles", "08:00", "09:30",
                "Cómputo", "Primaria"
        ));
        horarioCtrl.registrar(new Horario(
                "Jueves", "10:00", "11:30",
                "Regular", "Primaria"
        ));
        horarioCtrl.registrar(new Horario(
                "Viernes", "08:00", "09:00",
                "Regular", "Primaria"
        ));

        System.out.println("✓ "
                + horarioCtrl.total()
                + " horarios cargados.");
    }

    // ══════════════════════════════════════════════
    //  ALUMNOS
    // ══════════════════════════════════════════════
    private void cargarAlumnos() {
        System.out.println("Cargando alumnos...");

        // alumnos de secundaria
        alumnoCtrl.registrarAlumno(new Alumno(
                1, "Juan", "Pérez García",
                "74123456", "juan@gmail.com",
                "987100001", "Av. Lima 100",
                new Date(), "Secundaria",
                "3°", "A", new Date()
        ));
        alumnoCtrl.registrarAlumno(new Alumno(
                2,
                "Lucía",
                "Ramos Torres",
                "74234567",
                "lucia@gmail.com",
                "987100002",
                "Jr. Cusco 200",
                new Date(),
                "Secundaria",
                "3°",
                "A",
                new Date()
        ));
        alumnoCtrl.registrarAlumno(new Alumno(
                3,
                "Diego",
                "Flores Mendoza",
                "74345678",
                "diego@gmail.com",
                "987100003",
                "Av. Perú 300",
                new Date(),
                "Secundaria",
                "3°",
                "A",
                new Date()
        ));
        alumnoCtrl.registrarAlumno(new Alumno(
                4,
                "Sofía",
                "Castro Quispe",
                "74456789",
                "sofia@gmail.com",
                "987100004",
                "Calle Real 400",
                new Date(),
                "Secundaria",
                "4°",
                "B",
                new Date()
        ));
        alumnoCtrl.registrarAlumno(new Alumno(
                5,
                "Andrés",
                "Huanca López",
                "74567890",
                "andres@gmail.com",
                "987100005",
                "Jr. Bolívar 500",
                new Date(),
                "Secundaria",
                "4°",
                "B",
                new Date()
        ));

        // alumnos de primaria
        alumnoCtrl.registrarAlumno(new Alumno(
                6,
                "Valentina",
                "Díaz Morales",
                "74678901",
                "vale@gmail.com",
                "987100006",
                "Av. Colonial 600",
                new Date(),
                "Primaria",
                "3°",
                "A",
                new Date()
        ));
        alumnoCtrl.registrarAlumno(new Alumno(
                7,
                "Mateo",
                "Rojas Salazar",
                "74789012",
                "mateo@gmail.com",
                "987100007",
                "Jr. Arequipa 700",
                new Date(),
                "Primaria",
                "3°",
                "A",
                new Date()
        ));
        alumnoCtrl.registrarAlumno(new Alumno(
                8,
                "Isabella",
                "Vega Tello",
                "74890123",
                "isa@gmail.com",
                "987100008",
                "Calle Unión 800",
                new Date(),
                "Primaria",
                "4°",
                "B",
                new Date()
        ));
        alumnoCtrl.registrarAlumno(new Alumno(
                8,
                "Luna",
                "Aldave Palomino",
                "74890123",
                "lunaaldave@gmail.com",
                "987100008",
                "Calle 23 Prado",
                new Date(),
                "Primaria",
                "1",
                "A",
                new Date()
        ));

        System.out.println("✓ "
                + alumnoCtrl.totalAlumnos()
                + " alumnos cargados.");
    }

    // ══════════════════════════════════════════════
    //  APODERADOS
    // ══════════════════════════════════════════════
    private void cargarApoderados() {
        System.out.println("Cargando apoderados...");

        Apoderado apo1 = new Apoderado(
                1, "Roberto", "Pérez Salas",
                "40123456", "roberto@gmail.com",
                "987200001", "Av. Lima 100",
                new Date(), "Padre", "Ingeniero"
        );
        Apoderado apo2 = new Apoderado(
                2, "Carmen", "Torres Vega",
                "40234567", "carmen@gmail.com",
                "987200002", "Jr. Cusco 200",
                new Date(), "Madre", "Profesora"
        );
        Apoderado apo3 = new Apoderado(
                3, "Jorge", "Flores Ríos",
                "40345678", "jorge@gmail.com",
                "987200003", "Av. Perú 300",
                new Date(), "Padre", "Comerciante"
        );
        Apoderado apo4 = new Apoderado(
                4, "María", "Díaz Luna",
                "40456789", "maria@gmail.com",
                "987200004", "Calle Real 400",
                new Date(), "Madre", "Enfermera"
        );
        Apoderado apo6 = new Apoderado(
                4, "Sofia", "Palomino Carrion",
                "100606762", "palomino@gmail.com",
                "999201104", "Calle 23 Prado",
                new Date(), "Madre", "Doctora"
        );

        // asocia alumnos a apoderados
        List<Alumno> todosAlumnos
                = alumnoCtrl.listarTodos();

        // Lambda — busca alumno por código
        Alumno juan = todosAlumnos.stream()
                .filter(a -> a.getNombreCompleto()
                .contains("Juan"))
                .findFirst().orElse(null);
        Alumno lucia = todosAlumnos.stream()
                .filter(a -> a.getNombreCompleto()
                .contains("Lucía"))
                .findFirst().orElse(null);
        Alumno diego = todosAlumnos.stream()
                .filter(a -> a.getNombreCompleto()
                .contains("Diego"))
                .findFirst().orElse(null);
        Alumno sofia = todosAlumnos.stream()
                .filter(a -> a.getNombreCompleto()
                .contains("Sofía"))
                .findFirst().orElse(null);

        if (juan != null) {
            apo1.agregarAlumno(juan);
        }
        if (lucia != null) {
            apo2.agregarAlumno(lucia);
        }
        if (diego != null) {
            apo3.agregarAlumno(diego);
        }
        if (sofia != null) {
            apo4.agregarAlumno(sofia);
        }

        apoderadoCtrl.registrar(apo1);
        apoderadoCtrl.registrar(apo2);
        apoderadoCtrl.registrar(apo3);
        apoderadoCtrl.registrar(apo4);

        System.out.println("✓ "
                + apoderadoCtrl.total()
                + " apoderados cargados.");
    }

    // ══════════════════════════════════════════════
    //  PERÍODO ACADÉMICO (Año Escolar y Bimestres)
    // ══════════════════════════════════════════════
    /**
     * Crea el Año Escolar de prueba con las fechas típicas de los 4 bimestres.
     * NotaControlador y AsistenciaControlador ya no aceptan registros si el
     * bimestre correspondiente no está activo, así que este paso es obligatorio
     * antes de cargar notas o asistencias.
     */
    private void cargarPeriodoAcademico() {
        System.out.println("Configurando período académico...");

        Calendar cal = Calendar.getInstance();
        int anio = cal.get(Calendar.YEAR);
        periodoCtrl.crearAnioEscolar(anio);

        periodoCtrl.definirFechasBimestre(1,
                fecha(anio, Calendar.MARCH, 1),
                fecha(anio, Calendar.MAY, 31));
        periodoCtrl.definirFechasBimestre(2,
                fecha(anio, Calendar.JUNE, 1),
                fecha(anio, Calendar.AUGUST, 15));
        periodoCtrl.definirFechasBimestre(3,
                fecha(anio, Calendar.AUGUST, 16),
                fecha(anio, Calendar.OCTOBER, 31));
        periodoCtrl.definirFechasBimestre(4,
                fecha(anio, Calendar.NOVEMBER, 1),
                fecha(anio, Calendar.DECEMBER, 20));

        System.out.println("✓ Período académico configurado.");
    }

    private Date fecha(int anio, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes, dia, 0, 0, 0);
        return cal.getTime();
    }

    // ══════════════════════════════════════════════
    //  NOTAS
    // ══════════════════════════════════════════════
    private void cargarNotas() {
        System.out.println("Cargando notas...");

        List<Alumno> alumnos
                = alumnoCtrl.listarTodos();

        // Se activa el Bimestre 1 y se registran solo las notas
        // de ese bimestre, luego se avanza al Bimestre 2. Así se
        // respeta la misma regla que seguiría un docente real:
        // solo se puede registrar en el bimestre activo.
        periodoCtrl.activarBimestre(1);
        alumnos.forEach(a -> {
            String codigo = a.getCodigoAlumno();

            notaCtrl.registrar(new Nota(
                    1, 15.0, "Práctica 1",
                    "Matemática", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 16.0, "Práctica 2",
                    "Matemática", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 20, "Práctica 3",
                    "Matemática", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 19, "Participación",
                    "Matemática", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 14.0, "Examen",
                    "Matemática", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 16.0, "Práctica 1",
                    "Comunicación", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 11.0, "Práctica 2",
                    "Comunicación", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 12.0, "Práctica 3",
                    "Comunicación", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 20.0, "Participación",
                    "Comunicación", codigo
            ));
            notaCtrl.registrar(new Nota(
                    1, 13.0, "Examen",
                    "Comunicación", codigo
            ));
        });

        periodoCtrl.activarBimestre(2);
        alumnos.forEach(a -> {
            String codigo = a.getCodigoAlumno();

            notaCtrl.registrar(new Nota(
                    2, 17.0, "Práctica 1",
                    "Matemática", codigo
            ));
            notaCtrl.registrar(new Nota(
                    2, 12.0, "Examen",
                    "Ciencias", codigo
            ));
        });

        System.out.println("✓ Notas cargadas.");
    }

    // ══════════════════════════════════════════════
    //  ASISTENCIAS
    // ══════════════════════════════════════════════
    private void cargarAsistencias() {
        System.out.println("Cargando asistencias...");

        // Las asistencias de prueba usan fechas recientes (hoy y
        // los días previos), así que se activa el bimestre que
        // realmente contiene la fecha de hoy antes de registrar.
        boolean activado = periodoCtrl.activarBimestreQueContiene(new Date());
        if (!activado) {
            System.out.println(
                    "Aviso: la fecha de hoy no cae en ningún bimestre "
                    + "configurado; se omite la carga de asistencias de prueba."
            );
            return;
        }

        List<Alumno> alumnos = alumnoCtrl.listarTodos();

        // Agrupa a los alumnos por sección (nivel + grado + sección),
        // porque un RegistroAsistencia representa la asistencia de
        // TODA una sección en un curso y una fecha determinados
        // (igual que hace el docente al pasar lista por RegistroAsistenciaVista).
        Map<String, List<Alumno>> porSeccion = alumnos.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getNivel() + "|" + a.getGrado() + "|" + a.getSeccion()
                ));

        // Patrón de estados para simular una semana de prueba
        String[] patronEstados = {"P", "P", "T", "F", "J"};
        String[] patronObservaciones = {
            "", "", "Llegó 10 min tarde", "", "Certificado médico"
        };

        porSeccion.forEach((clave, listaAlumnos) -> {

            String[] partes = clave.split("\\|");
            String nivel = partes[0];
            String grado = partes[1];
            String seccion = partes[2];

            // Elige un curso y un docente representativos de ese nivel
            // para poder armar el RegistroAsistencia de prueba.
            List<Curso> cursosNivel = cursoCtrl.buscarPorNivel(nivel);
            List<Docente> docentesNivel = docenteCtrl.buscarPorNivel(nivel);

            if (cursosNivel.isEmpty() || docentesNivel.isEmpty()) {
                return; // no hay datos suficientes para esta sección
            }

            Curso curso = cursosNivel.get(0);
            Docente docente = docentesNivel.get(0);

            // Crea un RegistroAsistencia por cada día de la semana de prueba
            for (int dia = 0; dia < patronEstados.length; dia++) {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -dia);
                Date fecha = cal.getTime();

                RegistroAsistencia registro = new RegistroAsistencia(
                        fecha, curso.getIdCurso(), curso.getNombre(),
                        nivel, grado, seccion,
                        docente.getCodigoDocente(), docente.getNombreCompleto()
                );

                for (Alumno a : listaAlumnos) {
                    registro.agregarAsistencia(new Asistencia(
                            fecha, patronEstados[dia],
                            a.getCodigoAlumno(), patronObservaciones[dia]
                    ));
                }

                regAsisCtrl.registrar(registro);
            }
        });

        System.out.println("✓ Asistencias cargadas.");
    }
}
