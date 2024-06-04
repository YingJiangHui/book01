package org.ying.book.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.ying.book.pojo.SystemSetting;
import org.ying.book.service.SystemSettingsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system-settings")
public class SystemSettingsController {
    @Resource
    private SystemSettingsService systemSettingsService;
    @PatchMapping
    public void updateSystemSettings(@RequestBody List<SystemSetting> systemSettingList) {
        systemSettingsService.updateSystemSettings(systemSettingList);
    }

    @PostMapping
    public void saveSystemSettings(@RequestBody List<SystemSetting> systemSettingList) {
        systemSettingsService.updateSystemSettings(systemSettingList);
    }

    @GetMapping
    public List<SystemSetting> getSystemSettingList() {
        return systemSettingsService.getSystemSettingAll();
    }

    @GetMapping("/map")
    public Map<String, Object> getSystemSettingMap() {
        return systemSettingsService.getSystemSettingAllMap();
    }
}
