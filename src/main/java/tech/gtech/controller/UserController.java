package tech.gtech.controller;


import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import tech.gtech.domain.Users;
import jakarta.ws.rs.core.Response;
import tech.gtech.service.UserService;

import java.util.List;
import java.util.UUID;

import static org.hibernate.Hibernate.list;

@Path("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @POST
    public Response createUser(Users users){
        return Response.status(Response.Status.CREATED)
                .entity(userService.createUser(users))
                .build();
    }


    @GET
    public Response findAllUsers(
            @QueryParam("page") @DefaultValue("0") Integer page,
            @QueryParam("size") @DefaultValue("10") Integer size){

        List<Users> users = userService.findAllUsers(page, size);

        return Response.ok(users).build();

    }

    @GET
    @Path("/{id}")
    public Response findUserById(@PathParam("id") UUID id){
        return Response.ok(userService.findUserById(id)).build();
    }

    @Path("/hello")
    @GET
    public String helloWorld(){
        return "Hello World";
    }


    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") UUID id, Users user){
        return Response.ok(userService.updateUser(id, user)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") UUID id){
        userService.deleteUserById(id);
        return Response.noContent().build();
    }
}
