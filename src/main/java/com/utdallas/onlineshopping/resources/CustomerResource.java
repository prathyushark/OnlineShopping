package com.utdallas.onlineshopping.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.utdallas.onlineshopping.action.address.AddAddressAction;
import com.utdallas.onlineshopping.action.address.DeleteAddressAction;
import com.utdallas.onlineshopping.action.card.AddCardDetailAction;
import com.utdallas.onlineshopping.action.customer.ChallengeLoginAction;
import com.utdallas.onlineshopping.action.customer.CreateCustomerAction;
import com.utdallas.onlineshopping.action.customer.GetCustomerAction;
import com.utdallas.onlineshopping.action.customer.UpdateCustomerAction;
import com.utdallas.onlineshopping.payload.request.address.AddAddressRequest;
import com.utdallas.onlineshopping.payload.request.card.AddCardDetailRequest;
import com.utdallas.onlineshopping.payload.request.customer.ChallengeLoginRequest;
import com.utdallas.onlineshopping.payload.request.customer.CreateCustomerRequest;
import com.utdallas.onlineshopping.payload.request.customer.UpdateCustomerRequest;
import com.utdallas.onlineshopping.payload.response.address.AddressResponse;
import com.utdallas.onlineshopping.payload.response.card.CardDetailResponse;
import com.utdallas.onlineshopping.payload.response.customer.ChallengeLoginResponse;
import com.utdallas.onlineshopping.payload.response.customer.CustomerResponse;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class CustomerResource
{
    private final GetCustomerAction getCustomerAction;
    private final CreateCustomerAction createCustomerAction;
    private final ChallengeLoginAction challengeLoginAction;
    private final UpdateCustomerAction updateCustomerAction;
    private final AddAddressAction addAddressAction;
    private final DeleteAddressAction deleteAddressAction;
    private final AddCardDetailAction addCardDetailAction;

    @Inject
    public CustomerResource(Provider<GetCustomerAction> getCustomerActionProvider,
                            Provider<CreateCustomerAction> createCustomerActionProvider,
                            Provider<ChallengeLoginAction> challengeLoginActionProvider,
                            Provider<UpdateCustomerAction> updateCustomerActionProvider,
                            Provider<AddAddressAction> addAddressActionProvider,
                            Provider<AddCardDetailAction> addCardDetailActionProvider,
                            Provider<DeleteAddressAction> deleteAddressActionProvider)
    {
        this.getCustomerAction = getCustomerActionProvider.get();
        this.createCustomerAction = createCustomerActionProvider.get();
        this.challengeLoginAction = challengeLoginActionProvider.get();
        this.updateCustomerAction = updateCustomerActionProvider.get();
        this. addAddressAction = addAddressActionProvider.get();
        this.deleteAddressAction = deleteAddressActionProvider.get();
        this.addCardDetailAction = addCardDetailActionProvider.get();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Timed
    public Response get(@Context HttpHeaders headers, @PathParam("id") Long id)
    {
        CustomerResponse customerResponse = this.getCustomerAction.withId(id).invoke();
        return Response.ok().entity(customerResponse).build();
    }

    @POST
    @UnitOfWork
    @Timed
    public Response create(@Context HttpHeaders headers, @NotNull CreateCustomerRequest createCustomerRequest)
    {
        CustomerResponse customerResponse = this.createCustomerAction.withRequest(createCustomerRequest).invoke();
        return Response.status(Response.Status.CREATED).entity(customerResponse).build();
    }

    @POST
    @Path("/login")
    @UnitOfWork
    @Timed
    public Response challengeLogin(@Context HttpHeaders headers, @NotNull ChallengeLoginRequest challengeLoginRequest)
    {
        ChallengeLoginResponse challengeLoginResponse = this.challengeLoginAction.withRequest(challengeLoginRequest).invoke();
        return Response.status(Response.Status.OK).entity(challengeLoginResponse).build();
    }

    @POST
    @Path("/update/{id}")
    @UnitOfWork
    @Timed
    public Response update(@Context HttpHeaders headers, @NotNull UpdateCustomerRequest updateCustomerRequest, @NotNull @PathParam("id") Long id)
    {
        CustomerResponse customerResponse = this.updateCustomerAction.withId(id).withRequest(updateCustomerRequest).invoke();
        return Response.status(Response.Status.OK).entity(customerResponse).build();
    }

    /*
    * Address APIs
    * */

    @POST
    @Path("/address/{id}")
    @UnitOfWork
    @Timed
    public Response addAddress(@Context HttpHeaders headers, @NotNull AddAddressRequest addAddressRequest, @NotNull @PathParam("id") Long id)
    {
        AddressResponse addressResponse = this.addAddressAction.withRequest(addAddressRequest).forCustomerId(id).invoke();
        return Response.status(Response.Status.CREATED).entity(addressResponse).build();
    }

    @DELETE
    @Path("/address/{id}")
    @UnitOfWork
    @Timed
    public Response deleteAddress(@Context HttpHeaders headers, @NotNull @PathParam("id") Long id)
    {
        this.deleteAddressAction.withAddressId( id ).invoke();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /*
    * Card APIs
    * */

    @POST
    @Path("{id}/card")
    @UnitOfWork
    @Timed
    public Response addCardDetail(@Context HttpHeaders headers, @NotNull AddCardDetailRequest addCardDetailRequest, @NotNull @PathParam("id") Long id)
    {
        CardDetailResponse cardDetailResponse = this.addCardDetailAction.withRequest(addCardDetailRequest).forCustomerId(id).invoke();
        return Response.status(Response.Status.CREATED).entity(cardDetailResponse).build();
    }
}
