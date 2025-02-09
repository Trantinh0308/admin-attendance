package com.example.attendance.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ControlService {
    private Boolean modeAuto = true;
    private Boolean openStatus = false;
    private final SimpMessagingTemplate messagingTemplate;

    public ControlService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void saveModeControl(Boolean auto) {
        modeAuto = auto;
    }

    public void saveDoorStatus(Boolean open) {
        openStatus = open;
    }

    public void sendMessageControl(Boolean open) {
        messagingTemplate.convertAndSend("/topic/control", open);
    }

    public Boolean getModeControl() {
        return modeAuto;
    }

    public Boolean getOpenStatus() {
        return openStatus;
    }
}
