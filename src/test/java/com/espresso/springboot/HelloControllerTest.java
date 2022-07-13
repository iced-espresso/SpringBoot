package com.espresso.springboot;

import com.espresso.springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void hello가리턴됨() throws Exception{
        String hello = "hello";
        mvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
        mvc.perform(get("/getword"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        MockHttpServletRequestBuilder s = get("/getword");
        System.out.println(s);
    }

    @Test
    public void helloDto가리턴됨() throws  Exception{
        String name = "hello";
        int amount = 1000;
        mvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(name)))
                    .andExpect(jsonPath("$.amount",is(amount)));

        mvc.perform(get("/showlog"))
                .andExpect(status().isOk())
                .andExpect(content().string("hello,1000\n"));

        mvc.perform(get("/clrlog"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        mvc.perform(get("/clrloeg"))
                .andExpect(status().isNotFound());
    }
}
