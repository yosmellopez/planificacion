/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.planning.config.AppConfig;
import com.planning.controller.PlanController;
import com.planning.entity.Channel;
import com.planning.entity.ChildTask;
import com.planning.entity.PlTask;
import com.planning.entity.Position;
import com.planning.entity.Task;
import com.planning.entity.Users;
import com.planning.service.ChannelService;
import com.planning.service.ChildTaskService;
import com.planning.service.DocumentService;
import com.planning.service.PlTaskService;
import com.planning.service.PositionService;
import com.planning.service.TaskService;
import com.planning.service.UsersService;
import com.planning.util.MapeadorObjetos;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration(value = "web")
public class TestHibernate {

    @Autowired
    PlTaskService plTaskService;

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

    @BeforeClass
    public static void setUpClass() {
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

    @Test
    public void testSheduled() {
        Users users = usersService.findOne(15);
        ModelAndView view = controller.cargarDatosDiagrama(1013, users, new ModelMap());
        view.getModelMap();
    }

    private void buscarAntecesorasRecursivo(PlTask to) {
        elementos.add(to.getTask());
        List<ChildTask> childTasks = childTaskService.findByToAndIsChild(to, true);
        if (childTasks.isEmpty()) {
            return;
        }
        for (ChildTask childTask : childTasks) {
            buscarAntecesorasRecursivo(childTask.getFrom());
        }
    }
}
