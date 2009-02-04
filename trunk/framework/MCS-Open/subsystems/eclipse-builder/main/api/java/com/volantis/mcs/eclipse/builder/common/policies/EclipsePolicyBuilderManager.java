package com.volantis.mcs.eclipse.builder.common.policies;

import com.volantis.mcs.project.PolicyBuilderManager;

/**
 * An extension of PolicyBuilderManager used by Eclipse. Allows an underlying
 * EclipsePolicyBuilderManager to be used by implementations that only need
 * to override limited behaviour.
 */
public interface EclipsePolicyBuilderManager extends PolicyBuilderManager {
    /**
     * Returns true if the policy builder manager requires an underlying
     * instance of EclipsePolicyBuilderManagerImpl (the default implementation) to
     * operate. Should be checked when the PolicyBuilderManager is created, and
     * if it returns true, the underlying manager can be set with
     * {@link #setUnderlyingPolicyBuilderManager}.
     *
     * @return True if this implementation requires an underlying instance of
     *         EclipsePolicyBuilderManager
     */
    boolean requiresUnderlyingPolicyBuilderManager();

    /**
     * Set the underlying policy builder manager to use for default (unmodified)
     * operations. If no underlying policy builder manager is required, no
     * action should be taken.
     *
     * @param underlying The underlying policy builder
     */
    void setUnderlyingPolicyBuilderManager(EclipsePolicyBuilderManager underlying);
}
