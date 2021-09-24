package com.sonic.controller.controller;

import com.sonic.common.config.WebAspect;
import com.sonic.common.http.RespEnum;
import com.sonic.common.http.RespModel;
import com.sonic.controller.models.Steps;
import com.sonic.controller.models.http.StepSort;
import com.sonic.controller.services.StepsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhouYiXun
 * @des
 * @date 2021/9/19 11:45
 */
@Api(tags = "操作步骤相关")
@RestController
@RequestMapping("/steps")
public class StepsController {
    @Autowired
    private StepsService stepsService;

    @WebAspect
    @ApiOperation(value = "查找步骤列表", notes = "查找对应用例id下的步骤列表")
    @ApiImplicitParam(name = "caseId", value = "测试用例id", dataTypeClass = Integer.class)
    @GetMapping("/list")
    public RespModel<List<Steps>> findByCaseIdOrderBySort(@RequestParam(name = "caseId") int caseId) {
        return new RespModel(RespEnum.SEARCH_OK, stepsService.findByCaseIdOrderBySort(caseId));
    }

    @WebAspect
    @ApiOperation(value = "移出测试用例", notes = "将步骤从测试用例移出")
    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
    @GetMapping("/resetCaseId")
    public RespModel resetCaseId(@RequestParam(name = "id") int id) {
        if (stepsService.resetCaseId(id)) {
            return new RespModel(0, "移出测试用例成功！");
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }

    @WebAspect
    @ApiOperation(value = "删除操作步骤", notes = "将步骤删除，并且从所有公共步骤里移除")
    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
    @DeleteMapping
    public RespModel delete(@RequestParam(name = "id") int id) {
        if (stepsService.delete(id)) {
            return new RespModel(RespEnum.DELETE_OK);
        } else {
            return new RespModel(RespEnum.DELETE_ERROR);
        }
    }

    @WebAspect
    @ApiOperation(value = "更新操作步骤", notes = "新增或更新操作步骤")
    @PutMapping
    public RespModel save(@Validated @RequestBody Steps steps) {
        stepsService.save(steps);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "拖拽排序步骤", notes = "用于前端页面拖拽排序步骤")
    @PutMapping("/stepSort")
    public RespModel stepSort(@Validated @RequestBody StepSort stepSort) {
        stepsService.sortSteps(stepSort);
        return new RespModel(RespEnum.UPDATE_OK);
    }

    @WebAspect
    @ApiOperation(value = "查询步骤详情", notes = "查询对应步骤id的详情信息")
    @ApiImplicitParam(name = "id", value = "步骤id", dataTypeClass = Integer.class)
    @GetMapping
    public RespModel<Steps> findById(@RequestParam(name = "id") int id) {
        Steps steps = stepsService.findById(id);
        if (steps != null) {
            return new RespModel(RespEnum.SEARCH_OK, steps);
        } else {
            return new RespModel(RespEnum.ID_NOT_FOUND);
        }
    }
}