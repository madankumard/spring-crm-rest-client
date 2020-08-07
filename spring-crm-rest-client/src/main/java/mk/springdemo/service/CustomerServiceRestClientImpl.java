package mk.springdemo.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import mk.springdemo.model.Customer;

@Service
public class CustomerServiceRestClientImpl implements CustomerService {

	private RestTemplate restTemplate;
	private String crmRestUrl;
	private Logger logger = Logger.getLogger(getClass().getName());

	@Autowired
	public CustomerServiceRestClientImpl(RestTemplate restTemplate, @Value("${crm.rest.url}") String url) {
		this.restTemplate = restTemplate;
		this.crmRestUrl = url;
		logger.info("Loaded property: crm.rest.url=" + crmRestUrl);
	}

	@Override
	public List<Customer> getCustomers() {
		logger.info("in getCustomers(): Calling REST API " + crmRestUrl);
		ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(crmRestUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<Customer>>() {
				});

		List<Customer> customers = responseEntity.getBody();

		return customers;
	}

	@Override
	public void saveCustomer(Customer theCustomer) {
		logger.info("in saveCustomer(): Calling REST API " + crmRestUrl);

		int employeeId = theCustomer.getId();

		if (employeeId == 0) {
			// save
			restTemplate.postForEntity(crmRestUrl, theCustomer, String.class);
		} else {
			// update
			restTemplate.put(crmRestUrl, theCustomer);
		}
	}

	@Override
	public Customer getCustomer(int theId) {
		logger.info("in getCustomer(): Calling REST API " + crmRestUrl);

		Customer customer = restTemplate.getForObject(crmRestUrl + "/" + theId, Customer.class);

		return customer;
	}

	@Override
	public void deleteCustomer(int theId) {
		logger.info("in deleteCustomer(): Calling REST API " + crmRestUrl);

		restTemplate.delete(crmRestUrl + "/" + theId);
		
		logger.info("in deleteCustomer(): deleted customer theId=" + theId);

	}

}
