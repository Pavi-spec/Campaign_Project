package com.drive.core.models;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(
        adaptables = Resource.class,
        resourceType = "drive/components/calculator",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CalculatorModel {

    private static final Logger LOG =
            LoggerFactory.getLogger(CalculatorModel.class);

    @ValueMapValue
    private Double number1;

    @ValueMapValue
    private Double number2;

    @ValueMapValue
    private String operation;

    private String result;
    private String message;

    @PostConstruct
    protected void init() {

        LOG.trace("Entering CalculatorModel initialization");

        try {
            if (number1 == null || number2 == null
                    || operation == null || operation.isBlank()) {

                message = "Please configure both numbers and an operation.";
                LOG.warn("Calculator configuration is incomplete");
                return;
            }

            LOG.debug(
                    "Calculating: number1={}, number2={}, operation={}",
                    number1, number2, operation
            );

            double answer;

            switch (operation) {

                case "add":
                    answer = number1 + number2;
                    break;

                case "subtract":
                    answer = number1 - number2;
                    break;

                case "multiply":
                    answer = number1 * number2;
                    break;

                case "divide":
                    if (number2 == 0.0) {
                        message = "Cannot divide by zero.";
                        LOG.warn("Division by zero was requested");
                        return;
                    }
                    answer = number1 / number2;
                    break;

                default:
                    message = "Invalid calculator operation.";
                    LOG.warn("Unsupported operation: {}", operation);
                    return;
            }

            result = String.valueOf(answer);
            message = "Calculation completed successfully.";

            LOG.info("Calculator operation completed successfully");

        } catch (RuntimeException e) {
            message = "Calculation failed.";
            LOG.error("Unexpected calculator failure", e);
        } finally {
            LOG.trace("Exiting CalculatorModel initialization");
        }
    }
    public Double getNumber1() {
        return number1;
    }

    public Double getNumber2() {
        return number2;
    }

    public String getOperation() {
        return operation;
    }
    public String getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
