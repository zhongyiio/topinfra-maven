package top.infra.maven.extension.shared;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import top.infra.maven.CiOption;
import top.infra.maven.extension.CiOptionFactoryBean;

@Named
@Singleton
public class InfraOptionFactoryBean implements CiOptionFactoryBean {

    @Override
    public int getOrder() {
        return Orders.CI_OPTION_INFRA;
    }

    @Override
    public Class<?> getType() {
        return InfraOption.class;
    }

    @Override
    public List<CiOption> getOptions() {
        return Arrays.asList(InfraOption.values());
    }
}