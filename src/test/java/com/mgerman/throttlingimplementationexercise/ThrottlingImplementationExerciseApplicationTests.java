package com.mgerman.throttlingimplementationexercise;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
class ThrottlingImplementationExerciseApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test() {
        var ips = List.of("192.168.1.1, 192.168.1.2, 192.168.1.3");
        ips.parallelStream().forEach(
                ip -> {
                    for (int i = 0; i < 6; i++) {
                        try {
                            if (i < 2) {
                                this.mockMvc.perform(get("/").with(remoteHost(ip))).andDo(print()).andExpect(status().isOk());
                            } else {
                                this.mockMvc.perform(get("/").with(remoteHost(ip))).andDo(print()).andExpect(status().isBadGateway());
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        Thread.sleep( 60 * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < 6; i++) {
                        try {
                            if (i < 2) {
                                this.mockMvc.perform(get("/").with(remoteHost(ip))).andDo(print()).andExpect(status().isOk());
                            } else {
                                this.mockMvc.perform(get("/").with(remoteHost(ip))).andDo(print()).andExpect(status().isBadGateway());
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }

    private RequestPostProcessor remoteHost(String ip) {
        return request -> {
            request.setRemoteAddr(ip);
            return request;
        };
    }

}
