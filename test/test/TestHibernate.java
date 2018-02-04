/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.planning.config.AppConfig;
import com.planning.controller.PlanController;
import com.planning.entity.*;
import com.planning.service.*;
import com.planning.util.MapeadorObjetos;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration(value = "web")
public class TestHibernate {

    @Autowired
    PlTaskService plTaskService;

    @Autowired
    PlanService planService;

    @Autowired
    ManagementService managementService;

    @Autowired
    TaskService taskService;

    @Autowired
    PositionService positionService;

    @Autowired
    ChannelService channelService;

    @Autowired
    DocumentService documentService;

    ArrayList<Task> elementos = new ArrayList<>();

    @Autowired
    ChildTaskService childTaskService;

    @Autowired
    UsersService usersService;

    @Autowired
    MapeadorObjetos mapeadorObjetos;

    @Autowired
    PlanController controller;

    @Autowired
    GrupoRepository grupoRepository;

    @BeforeClass
    public static void setUpClass() throws IllegalStateException, NamingException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource("jdbc:oracle:thin:@localhost:1521:xe", "PLANNING", "adminsys26");
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind("java:comp/env/jdbc/planificacionDB", dataSource);
        builder.activate();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    //    @Test
    public void testSheduled() {
        Users users = usersService.findOne(15);
        ModelAndView view = controller.cargarDatosDiagrama(1013, users, new ModelMap());
        view.getModelMap();
    }

    @Test
    public void testGrupo() {
        plTaskService.findByPlan(new Plan(1013));
//        planService.findAll();
//        Task task = taskService.findOne(10);
//        List<Management> managements = managementService.findAll();
//        managements.forEach(System.out::println);
        //        List<Task> tasks = taskService.findAll();
        //        for (Task task : tasks) {
        //            System.out.println(task);
        //            System.out.println(task.getPosition());
        //            System.out.println(task.getPosition().getArea());
        //            System.out.println(task.getPosition().getArea().getManagement());
        //        }
        //        List<Grupo> grupos = grupoRepository.findByPlan(new Plan(1013));
        //        Set<Task> setTaskGrupo = grupos.parallelStream().map(grupo -> grupo.getTaskGrupo()).collect(Collectors.toSet());
        //        setTaskGrupo.parallelStream().forEach(task -> System.out.println(task.getName()));
        //        Grupo grupo = new Grupo(26, 27);
        //        grupoRepository.saveAndFlush(grupo);
        //        List<Grupo> groups = grupoRepository.findAll();
        //        for (Grupo group : groups) {
        //            System.out.println(group.getTaskGrupo().getName());
        //        }
        //        List<Grupo> grupoList = grupoRepository.findByTaskGrupo(task);
        //        List<Task> taskList = grupoList.parallelStream().map((grupo -> grupo.getTaskGrupo())).collect(Collectors.toList());
        //        List<Task> tasks = taskService.findAll();
        //        List<Task> noAgrupadas = tasks.parallelStream().filter(task1 -> !taskList.contains(task1)).collect(Collectors.toList());
    }

//    private void buscarAntecesorasRecursivo(PlTask to) {
//        elementos.add(to.getTask());
//        List<ChildTask> childTasks = childTaskService.findByToAndIsChild(to, true);
//        if (childTasks.isEmpty()) {
//            return;
//        }
//        for (ChildTask childTask : childTasks) {
//            buscarAntecesorasRecursivo(childTask.getFrom());
//        }
//    }
}
