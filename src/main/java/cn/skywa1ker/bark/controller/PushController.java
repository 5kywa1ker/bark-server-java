package cn.skywa1ker.bark.controller;

import cn.skywa1ker.bark.model.ApiResponses;
import cn.skywa1ker.bark.service.PushService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RequiredArgsConstructor
@RestController
public class PushController {

    private final PushService pushService;

    @GetMapping("/{deviceToken}/{title}/{content}")
    public ApiResponses<String> push(@PathVariable String deviceToken, @PathVariable String title,
        @PathVariable String content) {
        pushService.push(deviceToken, title, content);
        return ApiResponses.success("");
    }
}
