package cn.skywa1ker.bark.controller;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import cn.skywa1ker.bark.model.ApiResponses;
import cn.skywa1ker.bark.model.DeviceToken;
import cn.skywa1ker.bark.model.PushMessage;
import cn.skywa1ker.bark.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * message controller
 *
 * @author hfb
 * @date 20/6/2
 */
@Validated
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final PushService pushService;

    @GetMapping("/{key}")
    public ApiResponses<List<PushMessage>> pageByKey(@NotBlank(message = "key为空") @PathVariable String key,
        @PageableDefault Pageable pageable) {
        return ApiResponses.success(pushService.pageMessageByKey(key, pageable));
    }

    @GetMapping("/device/list")
    public ApiResponses<List<DeviceToken>> pageDevices(@PageableDefault Pageable pageable) {
        return ApiResponses.success(pushService.pageDevices(pageable));
    }

    @PostMapping("/device/updateRemark")
    public ApiResponses<Void> updateDeviceRemark(@NotBlank(message = "key为空") String key,
                                                 @Length(min = 1, max = 10, message = "remark长度1-10") String remark) {
        pushService.updateDeviceRemark(key, remark);
        return ApiResponses.success();
    }

}
