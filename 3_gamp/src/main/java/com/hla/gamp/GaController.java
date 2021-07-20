package com.hla.gamp;

import com.hla.gamp.ga.Ga4Service;
import com.hla.gamp.ga.UaService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class GaController {

    private final Ga4Service ga4Service;
    private final UaService uaService;

    public GaController(Ga4Service ga4Service, UaService uaService) {
        this.ga4Service = ga4Service;
        this.uaService = uaService;
    }

    @GetMapping("/ga4")
    public String ga4(@CookieValue(value = "_ga", required = false) String gaCookie, @RequestParam(value = "client_id", required = false) String clientId) {
        clientId = getClientId(gaCookie, clientId);
        ga4Service.sendEvent(clientId);
        return "index.html";
    }

    @GetMapping("/ua")
    public String ua(@CookieValue(value = "_ga", required = false) String gaCookie, @RequestParam(value = "client_id", required = false) String clientId) {
        uaService.sendEvent(getClientId(gaCookie, clientId));
        return "index.html";
    }

    private String extractClientId(String gaCookie) {
        try {
            String[] gaCookieComponents = gaCookie.split("\\.");
            return gaCookieComponents[2] + "." + gaCookieComponents[3];
        } catch (Exception e) {
            return null;
        }
    }

    private String getClientId(String gaCookie, String clientId) {
        if (!StringUtils.hasText(clientId)) {
            clientId = extractClientId(gaCookie);
        }
        if (!StringUtils.hasText(clientId)) {
            clientId = UUID.randomUUID().toString();
        }
        return clientId;
    }
}
