package com.paymybuddy.transactionapp.controller;

/*
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,classes = WavefrontProperties.Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
*/
public class ContactControllerIT {
 /*
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConnectedUserDetailsService connectedUserDetailsService;

    @Autowired
    UserAccountService userAccountService;

    @Before
    public void setUp(){
        userAccountService.createUser(new RegisterDto("user@example.com","user","user"));

    }

    @Test
    public void givenEmployees_whenGetEmployees_thenStatus200()
            throws Exception {

        mvc.perform(get("/contact")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("user")));
    }

    @WithMockUser("user")
    @Test
    public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/contact").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


*/
}
