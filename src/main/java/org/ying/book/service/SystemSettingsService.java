package org.ying.book.service;

import jakarta.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ying.book.dto.systemSettings.SystemSettingDto;
import org.ying.book.enums.SystemSettingsEnum;
import org.ying.book.mapper.SystemSettingMapper;
import org.ying.book.pojo.SystemSetting;
import org.ying.book.pojo.SystemSettingExample;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemSettingsService {
    @Resource
    private SystemSettingMapper systemSettingMapper;


    public List<SystemSetting> objectToMap(SystemSettingDto systemSettingDto) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Map<String, String> properties = BeanUtils.describe(systemSettingDto);

        return  properties.entrySet().stream().map(entry -> {
            SystemSetting systemSetting = new SystemSetting();
            systemSetting.setName(entry.getKey());
            systemSetting.setValue(entry.getValue());
            return systemSetting;
        }).toList();
    }

    @Transactional
    public void saveSystemSettings(List<SystemSetting> systemSettingList) {
        systemSettingList.forEach(systemSetting -> {
            systemSettingMapper.insertSelective(systemSetting);
        });
    }

    @Transactional
    public void updateSystemSettings(List<SystemSetting> systemSettingList) {
        systemSettingList.forEach(systemSetting -> {
            SystemSettingExample example = new SystemSettingExample();
            example.createCriteria().andNameEqualTo(systemSetting.getName());
            systemSettingMapper.updateByExampleSelective(systemSetting,example);
        });
    }

    public List<SystemSetting> getSystemSettingAll() {
        return getSystemSettingList(null);
    }

    public Map<String,Object> getSystemSettingAllMap() {
        List<SystemSetting> systemSettingList = getSystemSettingAll();
        Map<String,Object> map = new HashMap<>();
        systemSettingList.forEach(systemSetting -> {
            map.put(systemSetting.getName(), systemSetting.getValue());
        });
        return map;
    }

    public List<SystemSetting> getSystemSettingList(SystemSettingsEnum systemSettingsEnum) {
        SystemSettingExample example = new SystemSettingExample();
        if (systemSettingsEnum != null) {
            example.createCriteria().andNameEqualTo(systemSettingsEnum.name());
        }
        return systemSettingMapper.selectByExample(example);
    }

    public Object getSystemSettingValueByName(SystemSettingsEnum systemSettingsEnum) {
        List<SystemSetting> systemSettings = getSystemSettingList(systemSettingsEnum);
        if(systemSettings==null || systemSettings.isEmpty()) {
            throw new RuntimeException("系统设置不存在");
        }

        return systemSettings.get(0).getValue();
    }

}
