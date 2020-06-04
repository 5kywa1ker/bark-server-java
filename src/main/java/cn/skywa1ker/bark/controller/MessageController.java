package cn.skywa1ker.bark.controller;

import cn.skywa1ker.bark.model.ApiResponses;
import cn.skywa1ker.bark.model.DeviceToken;
import cn.skywa1ker.bark.model.PushMessage;
import cn.skywa1ker.bark.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

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
                                                     @Range(min = 1, max = 1000) int pageSize,
                                                     @Range(min = 0) int pageNo) {
        return ApiResponses.success(pushService.pageMessageByKey(key, PageRequest.of(pageNo, pageSize)));
    }

    @GetMapping("/device/list")
    public ApiResponses<List<DeviceToken>> pageDevices(@Range(min = 1, max = 1000) int pageSize,
                                                       @Range(min = 0) int pageNo) {
        return ApiResponses.success(pushService.pageDevices(PageRequest.of(pageNo, pageSize)));
    }

    @PostMapping("/device/updateRemark")
    public ApiResponses<Void> updateDeviceRemark(@NotBlank(message = "key为空") String key,
                                                 @Length(min = 1, max = 10, message = "remark长度1-10") String remark) {
        pushService.updateDeviceRemark(key, remark);
        return ApiResponses.success();
    }

}
