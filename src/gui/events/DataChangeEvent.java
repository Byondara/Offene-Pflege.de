/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.events;

import op.OPDE;
import org.apache.log4j.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.EventObject;
import java.util.Set;

/**
 * @author tloehr
 */
public class DataChangeEvent<T> extends EventObject {
    T data;

    public T getData() {
        return data;
    }

    private DataChangeEvent(Object source) {
        super(source);
    }

    public DataChangeEvent(Object source, T data) throws ConstraintViolationException {
        super(source);
        this.data = data;
        Validator validator = OPDE.getValidatorFactory().getValidator();

        Logger.getLogger(getClass()).debug(data);

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(this.data);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }


    }


}
