package top.infra.maven.extension.shared;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import top.infra.maven.CiOption;
import top.infra.maven.extension.CiOptionFactoryBean;

@Named
@Singleton
public class FastOptionFactoryBean implements CiOptionFactoryBean {

    @Override
    public int getOrder() {
        return Orders.CI_OPTION_FAST;
    }

    @Override
    public Class<?> getType() {
        return FastOption.class;
    }

    @Override
    public List<CiOption> getOptions() {
        return Arrays.asList(FastOption.values());
    }
}