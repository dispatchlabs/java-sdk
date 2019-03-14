package dispatchlabs.states;

import dispatchlabs.utils.AJson;

/**
 *
 */
public class Node extends AJson {

    /**
     * Class level-declarations.
     */
    private String address;
    private Endpoint httpEndpoint;

    /**
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     */
    public Endpoint getHttpEndpoint() {
        return httpEndpoint;
    }

    /**
     *
     * @param endpoint
     */
    public void setHttpEndpoint(Endpoint httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
    }
}
