// Code generated by protoc-gen-micro. DO NOT EDIT.
// source: services/pricing/proto/pricing.proto

package pricing

import (
	fmt "fmt"
	proto "github.com/golang/protobuf/proto"
	math "math"
)

import (
	context "context"
	client "github.com/micro/go-micro/client"
	server "github.com/micro/go-micro/server"
)

// Reference imports to suppress errors if they are not otherwise used.
var _ = proto.Marshal
var _ = fmt.Errorf
var _ = math.Inf

// This is a compile-time assertion to ensure that this generated file
// is compatible with the proto package it is being compiled against.
// A compilation error at this line likely means your copy of the
// proto package needs to be updated.
const _ = proto.ProtoPackageIsVersion3 // please upgrade the proto package

// Reference imports to suppress errors if they are not otherwise used.
var _ context.Context
var _ client.Option
var _ server.Option

// Client API for Pricing service

type PricingService interface {
	GetPrice(ctx context.Context, in *Request, opts ...client.CallOption) (*Response, error)
}

type pricingService struct {
	c    client.Client
	name string
}

func NewPricingService(name string, c client.Client) PricingService {
	if c == nil {
		c = client.NewClient()
	}
	if len(name) == 0 {
		name = "pricing"
	}
	return &pricingService{
		c:    c,
		name: name,
	}
}

func (c *pricingService) GetPrice(ctx context.Context, in *Request, opts ...client.CallOption) (*Response, error) {
	req := c.c.NewRequest(c.name, "Pricing.GetPrice", in)
	out := new(Response)
	err := c.c.Call(ctx, req, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

// Server API for Pricing service

type PricingHandler interface {
	GetPrice(context.Context, *Request, *Response) error
}

func RegisterPricingHandler(s server.Server, hdlr PricingHandler, opts ...server.HandlerOption) error {
	type pricing interface {
		GetPrice(ctx context.Context, in *Request, out *Response) error
	}
	type Pricing struct {
		pricing
	}
	h := &pricingHandler{hdlr}
	return s.Handle(s.NewHandler(&Pricing{h}, opts...))
}

type pricingHandler struct {
	PricingHandler
}

func (h *pricingHandler) GetPrice(ctx context.Context, in *Request, out *Response) error {
	return h.PricingHandler.GetPrice(ctx, in, out)
}
