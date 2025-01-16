package com.stockexchange.orderservice.controller.dto;

public record UserRequest(String username, String password, Boolean isAdmin) {}
